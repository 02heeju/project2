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
- Contact_list_item class
- menu_list_item
- MediaStoreAdapter
- bitmapconverter
등이 있습니다.  
