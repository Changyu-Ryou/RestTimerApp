# RestTimerApp for Android
> [App] 화면 on/off에 반응하는 타이머 앱

  <br/><br/>
## ✨ summary
스마트폰 화면의 ON/ OFF를 감지해 화면이 켜진 이후로부터 타이머가 작동되고, 타이머가 끝났을때 타임아웃 알림과 함께 화면을 OFF 하는 타이머입니다.
  <br/><br/>
## 📖 Introduction  
헬스나 운동 혹은 공부 중 일정 시간의 쉬는시간을 가질 때 대부분 핸드폰을 켜게되는데, 핸드폰을 사용하다 보면 생각치도 못하게 정한 쉬는시간을 넘어 핸드폰을 이용하게 되는 경우가 많습니다. 그런 일을 막고 정한만큼의 시간을 쉬었다는것을 알려주는 어플을 개발할 것입니다. 이번에는 Bottom Navigation을 이용하기 위한 Fragment를 사용했습니다.
  <br/><br/>
## 👨‍💻 System requirements
기본적으로 Android Studio에서 JAVA언어 기반으로 개발을 진행합니다.  
이를위해 Android Studio 설치가 필수적입니다.  
또한 안드로이드 SDK Android 8.1 오레오 API Level 27 을 타겟으로 개발합니다.
  <br/><br/>
## 📝 Todo list
제작할 코드와 문서들입니다.

- [x] [💻] 메인 액티비티에 Bottom Navigation 구현 (완료 ver 1.0)
- [x] [💻] 타이머 Fragment (완료 ver 1.0)
- [x] [💻] 오버레이 위젯 셋팅 Fragment (완료 ver 1.0)
- [x] [💻] 시작, 중지 버튼 구현 (완료 ver 1.0)
- [x] [💻] 타이머 셋팅 버튼 (완료 ver 1.0)
- [x] [💻] 위젯 구현 (완료 ver 1.0)
- [x] [📗] 핸들러를 통한 타이머 구현 (완료 ver 1.0)
- [x] [📗] 화면 ON/OFF 감지 (완료 ver 1.1)
- [x] [📗] 타임아웃 이벤트 구현 (완료 ver 1.1)
- [x] [📗] One Touch reset Timer widget 구현 (완료 ver 1.1)
- [x] [📗] Auto screen off setting switch (완료 ver 1.1)
- [x] [🔨] 오버레이 위젯 구현 (완료 ver 1.0)
- [x] [🔨] 오버레이 위젯 자유이동 (완료 ver 1.0)
- [x] [🔨] 오버레이 위젯 크기조절 SeekBar (완료 ver 1.0)
- [x] [🔨] 오버레이 위젯 배경색 변경 기능 (완료 ver 1.0)
- [X] [🔨] 오버레이 롱 클릭, 더블 클릭시 앱으로 이동 (완료 ver 1.0)
- [X] [🔨] 타이머 Fragment 내 google AdMob 배너 광고 삽입 (완료 ver 1.0)
- [X] [🔨] 셋팅 Fragment 내 google AdMob 배너 광고 삽입 (완료 ver 1.0)
- [X] [🔨] 사용자 가이드 팝업 액티비티 추가 (완료 ver 2.0)
- [X] [🔨] 오버레이 뷰 디자인 (완료 ver 2.0)
- [X] [🔨] 앱 전체적인 디자인 (완료 ver 2.0)
- [ ] [🔨] 로고 제작 
- [ ] [🔓] Play Store 출시 

  <br/> <br/>
  
## ☁️ Screen Shot
  
> ### ver 1.0 ( commit 2020-02-23 )  
메인 액티비티 구성 및 타이머 기능, 위젯 구현<br/>
위젯 기능은 이전 project였던 ServerTime의 소스를 재사용해 구현하였습니다.<br/>
<img src="https://user-images.githubusercontent.com/56837413/75100771-b178b000-5615-11ea-9112-388b28832395.jpg" width="30%"></img> 
<img src="https://user-images.githubusercontent.com/56837413/75100770-b0478300-5615-11ea-85c5-709c942a9f41.jpg" width="30%"></img>
  
  <br/>
  <br/>
  <br/>

> ### ver 1.1 ( commit 2020-02-24 )  
Screen ON/OFF 감지부 구현 <br/>
Screen Time Out Event 구현 <br/>
Auto screen off setting switch <br/>
One Touch reset Timer widget 구현 <br/>

<img src="https://user-images.githubusercontent.com/56837413/75116841-a1151380-56af-11ea-968d-695efb3528e0.gif" width="30%"></img>
 <img src="https://user-images.githubusercontent.com/56837413/75116844-a3776d80-56af-11ea-85b0-2cda415d6f71.gif" width="30%"></img> 
  
  <br/>
  <br/>
  <br/>

> ### ver 2.0 ( commit 2020-02-24 )  
사용 가이드 추가<br/>
메인 뷰 디자인<br/>
오버레이 위젯 디자인<br/>

<img src="https://user-images.githubusercontent.com/56837413/75151184-652e8c80-5749-11ea-930d-56b2d8f1fb18.jpg" width="30%"></img>
<img src="https://user-images.githubusercontent.com/56837413/75151186-665fb980-5749-11ea-956e-69697d85a269.jpg" width="30%"></img> 
  
  <br/>
  <br/>
  <br/>
  
 > ### ver 2.1 ( commit 2020-02-25 )  
로고 아이콘 추가   "Icon made by Freepik from www.flaticon.com" <br/>
출시를 위한 앱 설정 변경 <br/>
코드 정리 <br/>

<img src="https://user-images.githubusercontent.com/56837413/75168054-a171e500-5769-11ea-8d45-5946247db359.png" width="30%"></img>
  
  <br/>
  <br/>
  <br/>
  
  
  

