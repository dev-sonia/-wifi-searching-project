# Wifi-Searching-Project
*** 
## 프로젝트 소개
### 오픈 API를 활용한 내 위치 기반 공공 wifi 정보 제공 웹서비스 🌐

## :pushpin: 개발 스택 ##
- Java (JDK 1.8)
- Ultimate IntelliJ Edition
- SQLite 3.36.0.3
- Maven
- Web Application
- Ultimate IntelliJ Edition

## :pushpin: 사용 API ##
- [서울시 공공 와이파이 서비스 위치 정보](https://data.seoul.go.kr/dataList/OA-20883/S/1/datasetView.do)

## :pushpin: 디렉토리 구성 ##
```bash
├── database
│   └── test.db
│ 
├── dto
│   ├── History.java
│   └── Wifi.java
│  
├── service
│   ├── ApiService.java
│   ├── HistoryService.java
│   └── WifiService.java
│ 
└── pom.xml
```

## :pushpin: 프로젝트 기능 ##
- 공공 와이파이 정보 가져 오기 기능
- 내 위치 정보를 입력하면, 가까운 위치에 있는 20개 와이파이 정보 보여주기
- 입력한 정보를 조회하는 시점에 DB에 히스토리 저장 및 보여주기