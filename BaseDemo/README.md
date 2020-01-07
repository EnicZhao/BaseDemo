## 前言
项目采用MVP设计模式，搭建rxjava2+retrofit2+okhttp3网络框架，使用Androidx控件，项目中包含各种沉浸式状态栏、添加了Android6.0及以上权限管理等。 

## 网络框架
	rxjava2+retrofit2+okhttp3网络框架已经属于比较主流的网络框架，其主要优势简单易用，可添加拦截器对请求或请求结果做公共处理，日志拦截更能清晰的打印出请求的日志，包含请求url、参数以及请求结果。rxjava2主要用来处理异步以及线程之间的切换，retrofit2主要处理请求数据与请求结果的处理，okhttp3负责请求的过程，需要引入以下依赖：
	    implementation 'io.reactivex.rxjava2:rxjava:2.2.12'
	    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
	    implementation 'com.squareup.retrofit2:retrofit:2.7.0'
	    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.7.0'
	    implementation 'com.squareup.retrofit2:converter-gson:2.7.0' //将请求结果转化成实体类
	    implementation 'com.squareup.okhttp3:okhttp:4.2.2'
	    implementation 'com.squareup.okhttp3:logging-interceptor:4.2.2' //日志拦截器
	    implementation 'com.squareup.okio:okio:2.4.2' //短小精悍的IO框架
	    implementation 'com.trello.rxlifecycle3:rxlifecycle:3.1.0' //解决rxjava的内存泄漏的问提，下面会提到
	    implementation 'com.trello.rxlifecycle3:rxlifecycle-android:3.1.0'
	    implementation 'com.trello.rxlifecycle3:rxlifecycle-components:3.1.0'   

###### ApiEngine文件
		public class ApiEngine {
		    private volatile static ApiEngine apiEngine;
		    private static Retrofit retrofit;
		    private static final int requestTime = 20;//请求超时、读写时间
		    private ApiEngine() {
		        String domain_url = Config.API_DOMAIN; //base_url域名
		        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
		        OkHttpClient client = new OkHttpClient.Builder()
		                .connectTimeout(requestTime, TimeUnit.SECONDS)
		                .writeTimeout(requestTime, TimeUnit.SECONDS)
		                .readTimeout(requestTime, TimeUnit.SECONDS)
		                .addInterceptor(loggingInterceptor)//日志拦截器
		                .addInterceptor(new RequestInterceptor())//请求拦截器
		                .addInterceptor(new ResponseInterceptor())//响应拦截器
		                .build();
		        retrofit = new Retrofit.Builder()
		                .baseUrl(domain_url)
		                .client(client)
		                .addConverterFactory(GsonConverterFactory.create())
		                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		                .build();
		    }
		    public static ApiEngine init() {
		        if (apiEngine == null) {
		            synchronized (ApiEngine.class) {
		                if (apiEngine == null) {
		                    apiEngine = new ApiEngine();
		                }
		            }
		        }
		        return apiEngine;
		    }
		    public static Request getApiService() {//采用API方式进行数据请求
		        return retrofit.create(Request.class);
		    }
		}
###### Request文件
		public interface Request {
		    @Streaming//下载
		    @GET//get方式
		    Observable<ResponseBody> download(@Url String url);//下载地址
		    
		    @FormUrlEncoded//form请求，如果没有请求参数，则去掉
		    @POST(ConfigUtil.API.ANALYSIS_IMAGE)//请求的接口名称（POST请求）
		    Observable<AnalysisImageResult> analysisImage(@FieldMap Map<String, String> fields);//设计的请求本地接口方法名称以及参数
		    
		    @POST(ConfigUtil.API.UPLOAD_DATA)
		    Observable<UploadResponseBean> uploadData(@Body RequestBody body);//采用body方式请求，比较json格式请求
		}
###### 请求方式
		登录：
			public voidlogin(String account,String pwd){
		        Map<String,String> params = new HashMap<String, String>();
		        params.put("account",account);
		        params.put("password",pwdHex);
		        Request apiService = ApiEngine.getApiService();
		        apiService.login(params)
		                .compose(RxSchedulers.switchThread())//线程切换
		                .compose(view.bindToLife())//避免rxjava内存泄漏，将rxjava执行与activity或fragmeng生命周期进行绑定，具体可以参照baseActivity或baseFragment
		                .subscribe(new Observer<LoginResult>() {
		                    @Override
		                    public void onSubscribe(Disposable d) {
		                    }
		                    @Override
		                    public void onNext(LoginResult result) {//LoginResult 是请求的结果被Gson转换成实体类
		                      	//响应结果
		                    }
		
		                    @Override
		                    public void onError(Throwable e) {
		                    	//错误结果，比如404，500，超时等
		                    }
		
		                    @Override
		                    public void onComplete() {
		
		                    }
		                });
		    }
###### DownloadUtil文件
		    public void startDownload(File file,String ufis,String contentType) {//参数file下载保存的文件，ufis与contentType与下载文件属性相关，可根据自身需要进行修改
		        String url = Config.API_DOMAIN + Config.API.DOWNLOAD_FILE + "?fjid=" + ufis;
		        retrofit.create(Request.class).download(url)
		                .subscribeOn(Schedulers.io())
		                .unsubscribeOn(Schedulers.io())
		                .map(new Function<ResponseBody, InputStream>() {
		                    @Override
		                    public InputStream apply(ResponseBody responseBody) throws Exception {
		                        return responseBody.byteStream();
		                    }
		                })
		                .observeOn(Schedulers.computation()) // 用于计算任务
		                .doOnNext(new Consumer<InputStream>() {
		                    @Override
		                    public void accept(InputStream inputStream) throws Exception {
		                        writeFile(inputStream, file);
		                    }
		                })
		                .observeOn(AndroidSchedulers.mainThread()).subscribe();
		    }

## 基类设计  
###### IBaseView
	public interface IBaseView {//接口约束
	
	    void showLoading();//显示加载控件
	
	    void hideLoading();//隐藏加载控件
	
	    <T> LifecycleTransformer<T> bindToLife();//RxJava生命周期控制，防止内存泄漏
	}

###### IBaseModel
	public interface IBaseModel{
	//仅作约束作用
	}

###### AbstractBasePresenter
	public abstract class AbstractBasePresenter<M extends IBaseModel,V extends IBaseView> {//mvp设计模式中抽象类presenter
	    protected M model;
	    protected V view;
	
	    public void attach(V view){
	        this.view = view;
	        this.model = getModel();
	    }
	
	    public void deAttach(){
	        this.view = null;
	        this.model = null;
	    }
	    protected abstract M getModel();
	}
######  BaseActivity
	public abstract class BaseActivity<P extends AbstractBasePresenter> extends RxAppCompatActivity implements IBaseView, View.OnClickListener {
	    protected P presenter;
	    protected Activity currentActivity;
	    private NetStateChangeReceiver receiver;
	    private CommitLoadingPop loadingPop;
	
	    @Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        TouchEffectsFactory.initTouchEffects(this, TouchEffectsWholeType.NONE);
	        super.onCreate(savedInstanceState);
	        setContentView(_attachLayoutRes());
	        ButterKnife.bind(this);
	        currentActivity = this;
	        ActivityManagerUtil.addActivity(this);
	        presenter = getPresenter();
	        if (presenter != null){
	            presenter.attach(this);
	        }
	        initWindow();
	        _initView(savedInstanceState);
	        _initData();
	        initReceiver();
	    }
	
	    //监听网络变化的情况
	    private void initReceiver(){
	        IntentFilter intentFilter = new IntentFilter();
	        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
	        receiver = new NetStateChangeReceiver();
	        registerReceiver(receiver,intentFilter);
	    }
	
	    private void unRegisterReceiver(){
	        unregisterReceiver(receiver);
	    }
	
	    protected P getPresenter(){
	        return null;
	    }
	
	    @Override
	    public <T> LifecycleTransformer<T> bindToLife() {
	        return this.<T>bindUntilEvent(ActivityEvent.DESTROY);//绑定RXjava执行的生命周期到DESTROY，这个可以查看官方api查看使用规则，比较简单。
	    }
	
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        unRegisterReceiver();
	        if (presenter != null){
	            presenter.deAttach();
	            presenter = null;
	        }
	        ActivityManagerUtil.finishActivity(this);
	    }
	
	    private void initWindow() {
	        StatusBarUtil.setTranslucentForImageView(this,0,null);
	    }
	
	    protected void toActivity(Class<?> cls){
	        Intent intent = new Intent(this,cls);
	        startActivity(intent);
	    }
	
	    protected void toActivityForResult(Class<?> cls,int requestCode){
	        Intent intent = new Intent(this,cls);
	        startActivityForResult(intent,requestCode);
	    }
	
	    @Override
	    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
	        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	        if(grantResults.length > 0){
	            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
	                getPermissionResult(requestCode,true);
	            }else {
	                showWaringDialog(requestCode);
	            }
	        }else {
	            showWaringDialog(requestCode);
	        }
	    }
	
	    protected void requestPermission(int request){
	        String[] permissions = PermissionUtil.getPermissions(request);
	        if(PermissionUtil.hasPermission(currentActivity,permissions)){
	            getPermissionResult(request,true);
	        }else {
	            PermissionUtil.requestPermission(currentActivity,permissions,request);
	        }
	    }
	
	    protected void getPermissionResult(int requestCode,boolean result){
	
	    }
	
	    private void showWaringDialog(int request) {
	        String appName = getResources().getString(R.string.app_name);
	        String permissionName = PermissionUtil.getPermissionName(request);
	        new AlertDialog.Builder(this)
	                .setTitle("警告")
	                .setMessage("请在"+ appName + "权限中打开" + permissionName + "，否则功能无法正常运行！")
	                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        getPermissionResult(request,false);
	                    }
	                }).show();
	    }
	
	
	    protected abstract int _attachLayoutRes();
	    protected abstract void _initView(Bundle savedInstanceState);
	    protected abstract void _initData();
	
	    @Override
	    public void showLoading() {
	        if(loadingPop == null){
	            loadingPop = new CommitLoadingPop(currentActivity);
	        }
	        loadingPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
	    }
	
	    @Override
	    public void hideLoading() {
	        if(loadingPop != null){
	            loadingPop.dismiss();
	        }
	    }
	
	    @Override
	    protected void onPause() {
	        super.onPause();
	        hideLoading();
	    }
	
	    protected long lastClickTime = 0;//记录上次点击时间
	    protected boolean checkClickLimit(){
	        if(System.currentTimeMillis() - lastClickTime < Config.VALUE.CLICK_LIMIT_TIME){
	            return true;
	        }
	        lastClickTime = System.currentTimeMillis();
	        return false;
	    }
	
	    //过滤点击太快的问题
	    protected void setOnClickId(@IdRes int ...ids){
	        if(ids.length > 0){
	            for (int id : ids) {
	                findViewById(id).setOnClickListener(this);
	            }
	        }
	    }
	
	    @Override
	    public void onClick(View v) {
	        if(checkClickLimit()){
	            return;
	        }
	        doClick(v);
	    }

	    protected void doClick(View v){
	    }
	}
###### BaseFragment
	public abstract class BaseFragment<P extends AbstractBasePresenter> extends RxFragment implements IBaseView, View.OnClickListener {
	    protected Context context;
	    protected View rootView;
	    private Unbinder unbinder;
	    protected boolean isLoaded ;
	    protected P presenter;
	    protected FragmentEntity bundleData;
	    private CommitLoadingPop loadingPop;
	
	    @Nullable
	    @Override
	    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	        if (rootView == null) {
	            rootView = inflater.inflate(inflateLayoutRes(),container,false);
	            unbinder = ButterKnife.bind(this, rootView);
	            presenter = getPresenter();
	            if (presenter != null){
	                presenter.attach(this);
	            }
	            initView();
	        }
	        return rootView;
	    }
	
	    @Override
	    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        if(savedInstanceState != null){
	            bundleData = (FragmentEntity) savedInstanceState.getSerializable("data");
	        }
	    }
	
	    @Override
	    public void onResume() {
	        super.onResume();
	        if(!isLoaded){
	            isLoaded = true;
	            initData();
	        }
	    }
	
	    @Override
	    public void onSaveInstanceState(@NonNull Bundle outState) {
	        super.onSaveInstanceState(outState);
	        saveDataToInstanceState(outState);
	    }
	
	    protected void saveDataToInstanceState(@NonNull Bundle outState){
	        if(bundleData == null){
	            bundleData = new FragmentEntity();
	        }
	    }
	
	    @Override
	    public void onDestroyView() {
	        if(rootView != null){
	            ViewGroup parent = (ViewGroup) rootView.getParent();
	            if(parent != null){
	                parent.removeView(rootView);
	            }
	        }
	        super.onDestroyView();
	    }
	
	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        if(unbinder != null){
	            unbinder.unbind();
	        }
	        if(presenter != null){
	            presenter.deAttach();
	        }
	    }
	
	    protected void toActivity(Class<?> cls){
	        Intent intent = new Intent(context,cls);
	        context.startActivity(intent);
	    }
	
	    @Override
	    public void showLoading() {
	        if(loadingPop == null){
	            loadingPop = new CommitLoadingPop(getContext());
	        }
	        loadingPop.showAtLocation(rootView, Gravity.CENTER,0,0);
	    }
	
	    @Override
	    public void hideLoading() {
	        if(loadingPop != null){
	            loadingPop.dismiss();
	        }
	    }
	
	    @Override
	    public void onPause() {
	        super.onPause();
	        hideLoading();
	    }
	
	    @Override
	    public <T> LifecycleTransformer<T> bindToLife() {
	        return this.<T>bindUntilEvent(FragmentEvent.DESTROY_VIEW);
	    }
	
	    protected P getPresenter(){
	        return null;
	    }
	    protected abstract int inflateLayoutRes();//填充layout
	    protected abstract void initView();//初始化控件，数据加载及耗时操作不要在这个方法中执行
	    protected abstract void initData();//只有第一次进入该页面是才会调用，所有数据加载都应该在这里进行
	
	    protected long lastClickTime = 0;//记录上次点击时间
	    protected boolean checkClickLimit(){
	        if(System.currentTimeMillis() - lastClickTime < Config.VALUE.CLICK_LIMIT_TIME){
	            return true;
	        }
	        lastClickTime = System.currentTimeMillis();
	        return false;
	    }
	
	    //过滤点击太快的问题
	    protected void setOnClickId(@IdRes int ...ids){
	        if(ids.length > 0){
	            for (int id : ids) {
	                rootView.findViewById(id).setOnClickListener(this);
	            }
	        }
	    }
	
	    @Override
	    public void onClick(View v) {
	        if(checkClickLimit()){
	            return;
	        }
	        doClick(v);
	    }
	
	    protected void doClick(View v){
	
	    }
	
	    @Override
	    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
	        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	        if(grantResults.length > 0){
	            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
	                getPermissionResult(requestCode,true);
	            }else {
	                showWaringDialog(requestCode);
	            }
	        }else {
	            showWaringDialog(requestCode);
	        }
	    }
	
	    protected void requestPermission(int request){
	        String[] permissions = PermissionUtil.getPermissions(request);
	        if(PermissionUtil.hasPermission(getActivity(),permissions)){
	            getPermissionResult(request,true);
	        }else {
	            PermissionUtil.requestPermission(getActivity(),permissions,request);
	        }
	    }
	
	    protected void getPermissionResult(int requestCode,boolean result){
	
	    }
	
	    private void showWaringDialog(int request) {
	        String appName = getResources().getString(R.string.app_name);
	        String permissionName = PermissionUtil.getPermissionName(request);
	        new AlertDialog.Builder(getContext())
	                .setTitle("警告")
	                .setMessage("请在"+ appName + "权限中打开" + permissionName + "，否则功能无法正常运行！")
	                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        getPermissionResult(request,false);
	                    }
	                }).show();
	    }
	}
###### BaseApp
	public abstract class BaseApp extends Application {
	
	    private static Context context;
	    public static Context getAppContext(){
	        return context;
	    }
	    static {
	        TouchEffectsManager.build(TouchEffectsWholeType.SCALE)
	                .addViewType(TouchEffectsViewType.ALL);
	    }
	    @Override
	    public void onCreate() {
	        super.onCreate();
	        context = BaseApp.this;
	        init();
	    }
	
	    /**
	     * 公共工具类初始化
	     */
	    protected void init(){
	        SharePreferenceUtil.init();
	        ApiEngine.init();
	        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
	            @Override
	            public void accept(Throwable throwable) throws Exception {
	                Log.e("rxjava-->",throwable.getMessage());
	            }
	        });
	    }
	}
## 实体类
###### FragmentEntity 
该类的作用是为了保存fragment的数据存储，使用场景：当ViewPager包含多个fragment，默认情况下，ViewPager保存三个fragment状态，如A、B、C，当切换fragmen至Ct时，会销毁前一个fragment A，加载一个新的fragment D，当返回前一个fragment B时，又会重新创建之前的A,销毁D，这样就造成了重复创建fragment，并且重复加载数据，FragmentEntity 的作用就是避免当创建之前已经加载过的fragment时，直接获取该fragment已经加载过的数据，fragment使用Bundle保存数据，但是bundle不能保存Serializable的数组对象，所以使用Map存储更为合适。

	public class FragmentEntity implements IEntity{
		
	    public HashMap<String, Object> getData() {
	        return data;
	    }
	
	    public void setData(HashMap<String, Object> data) {
	        this.data = data;
	    }
	
	    private HashMap<String,Object> data;
	}
###### IEntity
	public interface IEntity extends Serializable {
	//所有的实体类最好实现该接口，作为实体类的约束作用
	}
###### ImplEntity
	public abstract class ImplEntity implements IEntity {
		//该类使用是为了做公共处理，需要做公共处理的可以继承该类
	}
## 接口
###### FragmentCallBack
	//fragment公共的回调
	public interface FragmentCallBack {
	    void toPager(int pager,boolean anim);//当在viewpager中页面切换
	}
###### OnRecyclerItemClickListener
	public interface OnRecyclerItemClickListener {
	    void click(int position);
	}
###### OnRecyclerLoadListener
	public interface OnRecyclerLoadListener {
	    void load();
	}
###### TopBarViewCallBack
	public interface TopBarViewCallBack {
	    void clickLeft();
	    void clickRight();
	    void clickBack();
	}
## RecyclerView
###### BaseRecyclerViewAdapter
该类的作用主要实现数组加载更多的功能，并且显示加载数据的状态，包含无数据、加载失败、加载全部与加载中状态，通过loadResult(int page,int rows,int total)方法来显示数组的加载状态，并过滤点击过快

	public abstract class BaseRecyclerViewAdapter<V extends ViewHolder> extends Adapter<ViewHolder> implements View.OnClickListener {
	    public static final int TYPE_FOOTER = -1001;
	    private static final int LOADING = 0;
	    private static final int EMPTY = 1;
	    private static final int ERROR = 2;
	    private static final int LOAD_ALL = 3;
	    private int currentStatus;
	    protected Context context;
	    protected LayoutInflater inflater;
	    private OnRecyclerLoadListener listener;
	    protected OnRecyclerItemClickListener onRecyclerItemClickListener;
	    protected boolean isLoading;
	    private boolean isInit = true;
	
	    public void setOnLoadListener(OnRecyclerLoadListener listener) {
	        this.listener = listener;
	    }
	
	    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
	        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
	    }
	
	    @Override
	    public int getItemViewType(int position) {
	        int count = getItemCount();
	        if(listener != null && position == count - 1){
	            return TYPE_FOOTER;
	        }
	        return getItemViewTypeChild(position);
	    }
	
	    @NonNull
	    @Override
	    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	        if (viewType == TYPE_FOOTER && inflater != null){
	            View view = inflater.inflate(R.layout.recycler_foot_view,parent,false);
	            return new FooterViewHolder(view);
	        }else {
	            return onCreateViewHolderChild(parent,viewType);
	        }
	    }
	
	    @Override
	    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
	        int type = getItemViewType(position);
	        if(type == TYPE_FOOTER){
	            FooterViewHolder holder = (FooterViewHolder) h;
	            if(currentStatus == EMPTY){
	                holder.tvHint.setText("暂无数据");
	                holder.tvHint.setVisibility(View.VISIBLE);
	                holder.pbLoading.setVisibility(View.GONE);
	            }else if(currentStatus == ERROR){
	                holder.tvHint.setText("数据加载异常");
	                holder.tvHint.setVisibility(View.VISIBLE);
	                holder.pbLoading.setVisibility(View.GONE);
	            }else if(currentStatus == LOAD_ALL){
	                holder.tvHint.setText("已加载全部数据");
	                holder.tvHint.setVisibility(View.VISIBLE);
	                holder.pbLoading.setVisibility(View.GONE);
	            }else{
	                holder.tvHint.setVisibility(View.GONE);
	                holder.pbLoading.setVisibility(View.VISIBLE);
	                if(!isLoading && !isInit){
	                    isLoading = true;
	                    if(listener != null){
	                        listener.load();
	                    }
	                }
	            }
	        }else {
	            V v = (V) h;
	            onBindViewHolderChild(v,position);
	        }
	    }
	
	    public void loadResult(int page,int rows,int total){
	        isInit = false;
	        if(total == -1){
	            showLoadError();
	        }else if(total == 0){
	            showEmpty();
	        }else {
	            if((page + 1) * rows >= total){
	                showLoadSuccess(false);
	            }else {
	                showLoadSuccess(true);
	            }
	        }
	    }
	
	    public void showLoadError(){
	        currentStatus = ERROR;
	        isLoading = false;
	        notifyDataSetChanged();
	    }
	
	    public void showEmpty(){
	        currentStatus = EMPTY;
	        isLoading = false;
	        notifyDataSetChanged();
	    }
	
	    public void showLoadSuccess(boolean hasMore){
	        if(!hasMore){
	            currentStatus = LOAD_ALL;
	        }else {
	            currentStatus = LOADING;
	        }
	        isLoading = false;
	        notifyDataSetChanged();
	    }
	
	    @Override
	    public int getItemCount() {
	        int count = getItemCountChild();
	        if(listener != null){
	            count ++;
	        }
	        return count;
	    }
	
	    protected abstract V onCreateViewHolderChild(@NonNull ViewGroup parent, int viewType);
	    protected abstract void onBindViewHolderChild(@NonNull V h, int position);
	    protected abstract int getItemCountChild();
	    protected abstract int getItemViewTypeChild(int position);
	
	
	    protected long lastClickTime = 0;//记录上次点击时间
	    protected boolean checkClickLimit(){
	        if(System.currentTimeMillis() - lastClickTime < Config.VALUE.CLICK_LIMIT_TIME){
	            return true;
	        }
	        lastClickTime = System.currentTimeMillis();
	        return false;
	    }
	
	    @Override
	    public void onClick(View v) {
	        if(checkClickLimit()){
	            return;
	        }
	        doClick(v);
	    }
	
	    protected void doClick(View v){
	
	    }
	}
###### CommonViewHolder
通用的ViewHolder

	public class CommonViewHolder extends RecyclerView.ViewHolder {
	
	    private final Context context;
	    private final SparseArray<View> mSparseArray;
	
	    public CommonViewHolder(@NonNull View itemView) {
	        super(itemView);
	        mSparseArray = new SparseArray<>();
	        context = itemView.getContext();
	    }
	
	    @SuppressWarnings("unchecked")
	    public final <T extends View> T getView(@IdRes int viewId) {
	        View view = mSparseArray.get(viewId);
	        if (view == null) {
	            view = itemView.findViewById(viewId);
	            mSparseArray.put(viewId, view);
	        }
	        return (T) view;
	    }
	
	    /********************************** 通用方法 ****************************************/
	    public void setVisibility(int viewId, int visibility) {
	        View view = getView(viewId);
	        view.setVisibility(visibility);
	    }
	
	    public void setSelected(int viewId, boolean selected) {
	        View view = getView(viewId);
	        view.setSelected(selected);
	    }
	
	    /********************************** TextView 常用函数 ****************************************/
	
	    public void setText(int viewId, CharSequence value) {
	        TextView view = getView(viewId);
	        view.setText(value);
	    }
	
	    public void setTextSize(int viewId, int testSize) {
	        TextView view = getView(viewId);
	        view.setTextSize(testSize);
	    }
	
	    public void setTextColor(int viewId, @ColorInt int textColor) {
	        TextView view = getView(viewId);
	        view.setTextColor(textColor);
	    }
	
	    /********************************** ImageView 相关函数 *****************************************/
	
	    public void setImageResource(int viewId, int imageResId) {
	        ImageView view = getView(viewId);
	        view.setImageResource(imageResId);
	    }
	
	    public void setImageDrawable(int viewId, Drawable drawable) {
	        ImageView view = getView(viewId);
	        view.setImageDrawable(drawable);
	    }
	
	    public void setImageBitmap(int viewId, Bitmap bitmap) {
	        ImageView view = getView(viewId);
	        view.setImageBitmap(bitmap);
	    }
	
	    /********************************** 常用监听 ****************************************/
	
	    public void setOnClickListener(int viewId, View.OnClickListener listener,int position) {
	        View view = getView(viewId);
	        view.setTag(position);
	        view.setOnClickListener(listener);
	    }
	
	    public void setOnTouchListener(int viewId, View.OnTouchListener listener) {
	        View view = getView(viewId);
	        view.setOnTouchListener(listener);
	    }
	
	    public void setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
	        View view = getView(viewId);
	        view.setOnLongClickListener(listener);
	    }
	
	    public void setOnItemClickListener(int position,View.OnClickListener listener) {
	        itemView.setTag(position);
	        itemView.setClickable(true);
	        itemView.setOnClickListener(listener);
	    }
	
	    public void onItenLongClickListener(int position,View.OnLongClickListener listener) {
	        itemView.setTag(position);
	        itemView.setLongClickable(true);
	        itemView.setOnLongClickListener(listener);
	    }
	}
###### CommonDividerItemDecoration
创建recyclerView的分割线，并且不显示最后一条分割线

	public class CommonDividerItemDecoration extends RecyclerView.ItemDecoration {
	
	    private Paint mPaint;
	    private Drawable mDivider;
	    private int mDividerHeight = 2;//分割线高度，默认为1px
	    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
	    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
	
	    /**
	     * 默认分割线：高度为2px，颜色为灰色
	     */
	    public CommonDividerItemDecoration(Context context, int orientation) {
	        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
	            throw new IllegalArgumentException("请输入正确的参数！");
	        }
	        mOrientation = orientation;
	
	        final TypedArray a = context.obtainStyledAttributes(ATTRS);
	        mDivider = a.getDrawable(0);
	        a.recycle();
	    }
	
	    /**
	     * 自定义分割线
	     */
	    public CommonDividerItemDecoration(Context context, int orientation,float dividerHeight,@DrawableRes int drawableId) {
	        this(context, orientation);
	        mDivider = ContextCompat.getDrawable(context, drawableId);
	        mDividerHeight = (int) dividerHeight;
	    }
	
	    /**
	     * 自定义分割线
	     */
	    public CommonDividerItemDecoration(Context context, int orientation, int dividerHeight) {
	        this(context, orientation);
	        mDividerHeight = dividerHeight;
	        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	        mPaint.setColor(context.getResources().getColor(R.color.app_theme_color));
	        mPaint.setStyle(Paint.Style.FILL);
	    }
	
	    //获取分割线尺寸
	    @Override
	    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
	        super.getItemOffsets(outRect, view, parent, state);
	        if(mOrientation == LinearLayoutManager.VERTICAL){
	            outRect.set(0, 0, 0, mDividerHeight);
	        }else {
	            int chidAdapterPosition = parent.getChildAdapterPosition(view);
	            int lastCount = parent.getAdapter().getItemCount() -1;
	            if(chidAdapterPosition != lastCount){
	                outRect.set(0, 0, 0, mDividerHeight);
	            }else {
	                outRect.set(0, 0, 0, 0);
	            }
	        }
	    }
	
	    //绘制分割线
	    @Override
	    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
	        super.onDraw(c, parent, state);
	        if (mOrientation == LinearLayoutManager.VERTICAL) {
	            drawVertical(c, parent);
	        } else {
	            drawHorizontal(c, parent);
	        }
	    }
	
	    //绘制横向 item 分割线
	    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
	        final int left = parent.getPaddingLeft();
	        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
	        final int childSize = parent.getChildCount();
	        if(childSize <= 1){
	            return;
	        }
	        for (int i = 0; i < childSize; i++) {
	            final View child = parent.getChildAt(i);
	            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
	            final int top = child.getBottom() + layoutParams.bottomMargin;
	            final int bottom = top + mDividerHeight;
	            if (mDivider != null) {
	                mDivider.setBounds(left, top, right, bottom);
	                mDivider.draw(canvas);
	            }
	            if (mPaint != null) {
	                canvas.drawRect(left, top, right, bottom, mPaint);
	            }
	        }
	    }
	
	    //绘制纵向 item 分割线
	    private void drawVertical(Canvas canvas, RecyclerView parent) {
	        final int top = parent.getPaddingTop();
	        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
	        final int childSize = parent.getChildCount();
	        for (int i = 0; i < childSize; i++) {
	            final View child = parent.getChildAt(i);
	            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
	            final int left = child.getRight() + layoutParams.rightMargin;
	            final int right = left + mDividerHeight;
	            if (mDivider != null) {
	                mDivider.setBounds(left, top, right, bottom);
	                mDivider.draw(canvas);
	            }
	            if (mPaint != null) {
	                canvas.drawRect(left, top, right, bottom, mPaint);
	            }
	        }
	    }
	}
## widget
###### TopBarView
该view主要为公共的头部标题显示样式，默认会有返回键，返回按钮默认是返回前一个页面作用，如果有其他需求，可以重写clickBack（）方法


## 如何快速继承框架
###### 引入架包
	   在app 的build.gradle中添加
	   在defaultConfig中添加 multiDexEnabled true;
	   并且在android{}中添加：
	    compileOptions {
	        sourceCompatibility JavaVersion.VERSION_1_8
	        targetCompatibility JavaVersion.VERSION_1_8
	    }
	    添加一下依赖
		implementation 'io.reactivex.rxjava2:rxjava:2.2.12'
	    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
	    implementation 'com.squareup.retrofit2:retrofit:2.7.0'
	    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.7.0'
	    implementation 'com.squareup.retrofit2:converter-gson:2.7.0'
	    implementation 'com.squareup.okhttp3:okhttp:4.2.2'
	    implementation 'com.squareup.okhttp3:logging-interceptor:4.2.2'
	    implementation 'com.squareup.okio:okio:2.4.2'
	    implementation 'com.trello.rxlifecycle3:rxlifecycle:3.1.0'
	    implementation 'com.trello.rxlifecycle3:rxlifecycle-android:3.1.0'
	    implementation 'com.trello.rxlifecycle3:rxlifecycle-components:3.1.0'
	    implementation 'com.jakewharton:butterknife:10.2.0'
	    annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.0'
	    implementation 'androidx.recyclerview:recyclerview:1.1.0'
	    implementation 'com.github.tbruyelle:rxpermissions:0.10.2'
	    implementation 'androidx.viewpager2:viewpager2:1.0.0'
	    implementation 'androidx.multidex:multidex:2.0.1'
	    implementation 'com.github.likaiyuan559:TouchEffects:0.4.0'

	配置项目的build.gradle
	buildscript {
	    repositories {
	        google()
	        jcenter()
	        mavenCentral()
	    }
	    dependencies {
	        classpath 'com.android.tools.build:gradle:3.5.3'
	        classpath 'com.jakewharton:butterknife-gradle-plugin:10.2.0'
	    }
	}
	
	allprojects {
	    repositories {
	        google()
	        jcenter()
	        maven { url 'https://jitpack.io' }
	    }
	}
	
	task clean(type: Delete) {
	    delete rootProject.buildDir
	}
###### 配置资源文件
	直接将res文件加复制到项目中，不过需要修改String文件中的app_name，修改xml文件夹中的文件，将external-path修改成自己项目中的命名，在manifest中添加：（Android7.0以上）
	       <provider
	            android:name="androidx.core.content.FileProvider"
	            android:authorities="com.project.enic.paysystemproject.fileProvider"
	            android:exported="false"
	            android:grantUriPermissions="true">
	            <meta-data
	                android:name="android.support.FILE_PROVIDER_PATHS"
	                android:resource="@xml/provider_path" />
        </provider>

## 复制基础文件
直接将项目中的base_module复制到项目文件中，并全局修改包名为自己的项目中的包名