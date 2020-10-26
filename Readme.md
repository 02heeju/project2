# Mad Camp Project 2  

Project Term : 7/8 ~ 7/15   
TeamMates : 02heeju , auaicn
<!-- @mentions auaicn   -->

## 서비스 소개

주어진 cloud(server) 를 사용하여,  
연락처와 사진갤러리를 각각 탭 layout 의 첫번째 탭과 두번째탭에 구현하였습니다.  
세번째 탭에는 서버를 이용한 서비스로서  

서비스 내용 : 
서버의 게시판에 같이 밥먹을 사람을 구하는 공고를 주문시각, 희망 인원수, 종목을 올리면, 다른 사용자들이 참여할 수 있도록 하였습니다.

## 사용된 기술 스택

Java, Retrofit, NodeJS, MongoDB, xml, git  

## Version Control

used 
- windows git-bash
- github desktop 
- android-studio internal version control system.
  
__earlier setting__  
1. new remote empty repository 의 생성
2. git clone to local repository
3. 해당 directory 에 안드로이드 스튜디오 프로젝트 생성
4. initial commit & push

이후 merge visualize tool 로는 안드로이드 스튜디오에서 기본적으로 제공하는 VCS 를 사용하였습니다.

## Implementation 

SplashActivity 에서는, build.gradle에서 명시된 모든 권한설정을 요구합니다.
  
모든 권한을 허용받은 이후, 
LoginActivity 가 실행이 됩니다.  

로그인은 두가지 방법을 통해 가능하도록 구현하였습니다.  
1. service-specific ID(email) / password
Android Studio EditText Form 을 통해, email, password field 를 입력 받은 후,  
server에 login query를 보내, email 의 존재여부와, password 가 일치하는지를 확인하여 로그인이 되도록 구현하였습니다.  
사용자 이름은 겹칠 수 있다고 판단하여, mongoDB 에서 로그인정보 DB의 unique key는 email address로 설정하였습니다.  
registration page 에서는 name field 를 추가로 받습니다.  

2. facebook indirect login
[facebook developer website](developer.facebook.com) 에서 권한을 받고, API 를 이용하여,  
facebook Login Button 을 구현하였습니다. facebook login callback 에서 login success 시 주는 token이 MainActivity 간에 유지가 되도록 구현하였습니다.  

Mainactivity 아래 세 개의 Fragment 를 구성하였습니다.  
1. ContactFragment
2. GalleryFragment
3. Tab3Fragment

Fragment 간의 이동은 ViewPager 를 통하여 구현하였습니다.

추가적인 Activity 로서는 
- VideoPlayActivity
- FullScreenImageActivity

추가적인 소스코드로는  
Customized List 를 구현하기 위한, 
-- Contact_list_item class
-- menu_list_item
- MediaStoreAdapter
- bitmapconverter



floating sheet
bottom_sheet_dialog
FACEBOOK login 


1. 

<h2> Notes </h2>  
<h3> 1. Activites & Fragments  </h3>
  
Activity 에는,  
1. 권한 요청을 위한 SplashActivity
2. 기본화면을 위한 MainActivity
3. 첫번째 탭 주소록 구현을 위한 profile_description 액티비티
4. 세번재 탭에서 구현한, To-do List의 항목별 세부사항을 위한 액티비티
가 있습니다.
  
탭 구조를 구현하기 위해서, android 에 기본 내장되어 있는 `material design` 의 TabLayout 을 사용하였습니다.  
`ViewPager`를 이용하여, ViewPagerAdapter에 추가해놓은 각 탭의 Fragment가 화면에 표시 될 수 있도록, MainActivity.java의 onCreate() 에서 처리해주었습니다.  
탭 3에서 to-do List에는 SQLite Database를 사용하였으며, 이를 위한 초기화 또한 MainActivity에서 진행하였습니다.  
  
추가로, 연락처 세부정보에서, 연락처의 편집을 위한 Fragment가 있습니다.  
  
<h3> 2. About Permission  </h3> 
  
AndroidManifest.xml 파일에  Contacts, External Storage, Direct_Dial 등 앱 실행에 필요한 권한들을 명시해 두었습니다.  
`AndroidManifest.xml:`  
![Alt text](https://github.com/auaicn/common_assignment/blob/master/images/permission.png)  
실제 권한의 요청이 일어나는 부분은 SplashFragment에 구현이 되어 있습니다.   
MainActivity에 들어가기 이전, SplashFragment 에서 명시된 모든 권한을 요청합니다.  
권한과 관련된 모든 요청을 확인 받은 경우에만 어플리케이션을 실행합니다.  
하나라도 거부되는 경우, 어플리케이션을 종료합니다.  

```
SplashFragment.java:  
    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE
    };
```

<!-- ![Alt text](https://github.com/auaicn/common_assignment/blob/master/images/permission_array.png)  -->
SplashActivity는 여러 handler 작동이 성공적으로 실행될 경우, MainActivity를 시작하고, 종료됩니다.  
        
<h2> 1. 첫번째 탭 </h2>  

<h3> Subject : 나의 연락처 구축 </h3>

휴대폰의 'Contacts' 어플리케이션의 contentProvider 에 query를 보내, update, read operation 을 수행하였습니다.  
query 과정에서, 연락처 content provider API를 통해 얻을 수 있는
1. `Starred` 값으로 먼저 sorting 한후, 
2. 사전 순으로 정렬된 상태로 가져올 수 있도록 하였습니다.  
  
`custom listView adapter`를 구현하여, 쿼리의 결과를 listView의 각 항목으로 불러오는 과정에서,  
`Starred` 가 "1"인 연락처들의 경우 "즐겨찾기" TextView 하나를 위에 추가하였으며,  
`Starred` 가 "0"인 않은 연락처들의 처음으로 나오는 "성" 을 보여주는 TextView를 추가하였습니다.  
  
![Alt text](https://github.com/auaicn/common_assignment/blob/master/images/additional_view.png)  

`TextWatcher`를 사용하여, 연락처 검색과, 실시간 결과 반환이 가능하도록 구현하였습니다.   
  
![Alt text](https://github.com/auaicn/common_assignment/blob/master/images/search_example.png)  

항목을 누를경우, onItemClickListener 를 설정하여, profile_description Activity가 Intent를 이용하여 시작될 수 있도록 하였습니다.  
Intent로 선택된 항목과 관련된 세부 정보들을 전달하였으며, TextView.setText()를 통해 화면에 표시될 텍스트를 설정해주었습니다.   
  
![Alt text](https://github.com/auaicn/common_assignment/blob/master/images/profile_description.png)  

전화, 메시지, 이메일 버튼이 있으며, 클릭 시 Intent를 이용하여 해당되는 기본 어플리케이션으로 하여금 전화, 메시지 전송, 이메일 기능을 수행할 수 있도록 구현하였습니다.  
이름 옆의 별 모양과, 가장 하단의 "즐겨찾기에 추가" 버튼을 누르면, 연락처 목록에서 상단에 위치할 수 있도록 하였습니다.  

```
profile_description.java:onCreate():
{
    ...
    // Direction
    direct_call = findViewById(R.id.dial_button);
    direct_call.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          Log.d("Profile Description","On Click. Text is [" + text_NUMBER.getText().toString().split("\n")[0] + "]");
          if(text_NUMBER.getText().equals("")){
              // Toast.makeText(getActivity(),"선택 : " + item.getName(), Toast.LENGTH_LONG).show();
              Log.d("Profile Description","On Direct call click, no Phone number. Send Toast message");
              Toast.makeText(view.getContext(),
                      "전화번호가 없습니다."
                      ,new Integer(1000)).show();
          }else{
              Log.d("Profile Description","On Direct call click, Phone number exists. Starting Activity");
              String primary_contact_number = "tel:" + text_NUMBER.getText().toString().split("\n")[0];
              startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + text_NUMBER.getText().toString().split("\n")[0].replaceAll("[a-b,A-Z, ,-]",""))));
          }
      }
    });
```

<h2> 2. 두번째 탭 </h2>

Subject : 나의 갤러리 구축

구글에서 공개한 이미지 라이브러리인 `Glide`를 이용하여 기기의 `Environment class`의 변수값으로 부터 사진이 있는 directory 정보를 얻어내고,  
해당 디렉토리 내 모든 이미지의 URI를 구하여, glide 를통해, image View에 mapping 하였습니다.  
각 사진의 image View를 gridView adapter를 통해, grid View의 항목으로 설정하여 화면에 표시되도록 구현하였습니다.  

```
photoFragment.java:onResume():  
{
    ...
    filesList = new ArrayList<>();
    addImagesFrom(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));

    GalleryAdapter galleryAdapter = new GalleryAdapter();

    // 데이터 설정 후, setAdapter
    galleryAdapter.setData(filesList);
    gridView.setAdapter(galleryAdapter);
```
        
![Alt text](https://github.com/auaicn/common_assignment/blob/master/images/gallery_example.png)  
layout 의 visibility을 활용하여 gridView.onItemClickListener 설정 내부에서, 큰 사진을 화면에 표시할 수 있도록 하였습니다.  

```
photoFragment.java:onCreateView():  
gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("hamApp", "onItemClick1");
                Glide.with(getActivity()).load(gridView.getAdapter().getItem(i)).into(selectedView);
                fullPhoto.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.INVISIBLE);
            }
        });
```
  
GridView로 돌아가는 Button 과, 해당 `onClickListener` 또한 구현하였습니다.  

<h2> 3. 세번째 탭 </h2>

<h3> Subject : 자유 주제 </h3>  

To-do List를 구현하였습니다.  
1. To-do List item은, 안드로이드에서 기본적으로 제공하는 sqlite.SQLiteDatabase 클래스를 사용하였습니다.  
2. List는 Recycler View를 이용하여 구현하였습니다.  
3. Calendar class를 사용하여, 해야할 일을 끝내야 하는 날짜를 정할 수 있도록 구현하였습니다.  
4. onItemClickListener를 설정하여, to-do List item에 대해, 클릭시 스크롤을 통해 종료 날짜를 수정할 수 있도록 하였습니다.  
5. 해야할 일을, 현재 날짜에서 가까운 것 우선으로 상단에 배치하였습니다.  
6. 완료한 할 일에 대해, 성취도를 설정할 수 있도록, 여러번 터치 시, 체크박스의 이미지를 변화시킬 수 있는 기능을 추가하였습니다.  

<h3>About DataBase Creation </h3>  
  
Application Install 마다 유지되는 하나의 Database를 생성하였습니다.   
openOrCreateDatabase 함수는, 해당 경로의 Database가 있을 경우에는, db를 열어주며, 없는 경우, 생성후 열어줍니다.  
Database 경로는, 안드로이드 OS내부의 SQLite 프로그램이 db파일을 모아두는 폴더에,  
사용자가 정의한 databaseFileName을 append한 String과 같습니다.  

```
MainActivity.java:init_database:
{    
    ...

    File file = new File(getFilesDir(), databaseFileName);
    try {
        db = SQLiteDatabase.openOrCreateDatabase(file, null);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    ...
    return db;
}
```

Table 은, 4개의 column을 가지도록 구성하였습니다.  

```
MainActivity.java:init_tables:
{    
    ...
    String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS "+ tableName + " (" +
        "NUM "      + "INTEGER NOT NULL," +
        "TITLE "    + "STRING NOT NULL," +
        "DATE "     + "STRING NOT NULL," +
        "EMOTION "  + "INTEGER NOT NULL" + ")";
```

onClick Events
1. To-do List checkbox
    
![Alt text](https://github.com/auaicn/common_assignment/blob/master/images/emotions.png)  




2. To-do List Item  
클릭시, TodoDetailActivity 를 시작합니다.  
Intent putExtra를 통해, `todoFragment`에서, `TodoDetailActivity` 에게 클릭된 항목의 database field 값을 전달합니다.  
전달 받은 값으로 `android.widget.datePicker` Class의 객체를 생성합니다.  
GregorianCalendar의 month enum의 January는 0에서 시작하기 때문에, 1을 더해주는 과정을 추가하였습니다.  

![Alt text](https://github.com/auaicn/common_assignment/blob/master/images/scroll_to_date_change.png)  

```
TodoDetailActivity.java:editClick():
{
    ...
    // Get newTitle 
    String newTitle = titleEditText.getText().toString();
    
    // Get newDate
    String newDate = datePicker.getYear() + "/" + (datePicker.getMonth()+1) + "/" + datePicker.getDayOfMonth();
    if(newTitle.replace(" ", "").equals("")) {
        Toast.makeText(this, "title이 비어있어요.", Toast.LENGTH_LONG).show();
        return;
    }
    
    // set SQL update statement
    String sqlInsert = "UPDATE CONTACT " +
            "SET TITLE = " + "'" + newTitle + "', " +
            "DATE = " + "'" + newDate + "' " +
            "WHERE NUM = " + numText.getText().toString();

    // actual query to Database
    todoDB.execSQL(sqlInsert);

    // Tell whether query succeedd
    setResult(RESULT_OK);
    
    // Finish the Activity
    finish();
}
```

스크롤을 통해, `Year`, `Month`, `Day`의 값을 조정할 수 있으며 `datePicker` 객체의 값을 변경할 수 있습니다.  
항목의 내용도 바꿀 수 있으며, 최종적으로 sql query를 통해 두 정보를 update 하고 activity를 종료합니다.  
```
{
    SQLiteDatabase todoDB;
    
    todoFragment:onResume()
    {
        ...
        todoDB.execSQL(sql_statement);
        adapter.notifyDataSetChanged();
        ...
    }
}
```
모든 sql문 실행 뒤에는, DB synchronization을 위한, RecyclerView adapter의 함수를 실행시켜 주어야 합니다.  

<h2> 4. Extras </h2>
  
어플리케이션 아이콘, 처음 실행시의 애니메이션, 전체적인 어플리케이션 내부 색상을 맞추기 위하여, resource,values folder에 `colors.xml`, `styles,xm`l 파일 내용을 변경하였으며,  
textview를 상속하는 여러 view들의 글꼴을 맞추기 위하여 font 파일들을 `resource/font` directory에 추가하였습니다.  
