package service;

import dto.Wifi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WifiService {

	private static final String JDBC = "org.sqlite.JDBC";
	private static final String URL = "jdbc:sqlite:D:\\BOOTCAMP\\project\\wifi-searching-project\\src\\main\\database\\test.db";

	// select - 가까운 20개 와이파이 조회 함수
	public List<Wifi> list(String latitude, String longitude) {
		List<Wifi> list = new ArrayList<>();

		try {
			Class.forName(JDBC);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = DriverManager.getConnection(URL);
			System.out.println("SQLITE DB Connected!");

			// 현 위치와 DB 상의 위,경도 거리 계산하는 서브 쿼리
			String subsql = "6371*acos(cos(radians("+ longitude +"))*cos(radians(lat))*cos(radians(lnt) - radians( " +latitude+ "))+ sin(radians("+ longitude +"))*sin(radians(lat)))";
			
			// select 조건문에 거리 연산 넣기
			// DISTANCE는 소수점 3자리까지 출력하고 20개 띄우기
			String sql = " select *, round(( " + subsql+ " ),3) as distance"
							+ " from wifi_info "
							+ " order by distance "
							+ " limit 20; ";
			
			preparedStatement = connection.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while(rs.next()) {       
                Wifi wifi = new Wifi();
                
                wifi.setDistance(rs.getString("distance")); // 거리 연산 결과
                wifi.setWifiNum(rs.getString("num"));
                wifi.setGu(rs.getString("gu"));
                wifi.setName(rs.getString("name"));
                wifi.setAddress1(rs.getString("adres1"));
                wifi.setAddress2(rs.getString("adres2"));
                wifi.setFloor(rs.getString("floor"));
                wifi.setType(rs.getString("type"));
                wifi.setOrg(rs.getString("org"));
                wifi.setSvc(rs.getString("svc"));
                wifi.setMangType(rs.getString("mang_type"));
                wifi.setInstallYear(rs.getString("install_year"));
                wifi.setInout(rs.getString("inout"));
                wifi.setWifiEnv(rs.getString("wifi_env"));
                wifi.setLat(rs.getString("lat"));
                wifi.setLnt(rs.getString("lnt"));
                wifi.setWorkDay(rs.getString("work_day"));
                
                list.add(wifi);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
                if ( rs != null ){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
			
			try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if ( connection != null && !connection.isClosed() ) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
		return list;
	}

	// saveDB - 파싱한 데이터를 DB에 저장하는 함수
	public void saveDB(List<Wifi> wifiList) {

		try {
			Class.forName(JDBC);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DriverManager.getConnection(URL);
			System.out.println("SQLITE DB Connected!");
			
			// 1. Obtain connection and set `false` to autoCommit
            // connection.setAutoCommit(false);

			 String sql = " insert  into wifi_info (num, gu, name, adres1, adres2, floor, type, org, svc, mang_type, install_year, inout, wifi_env, lat, lnt, work_day) "
	                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";
			
			for (Wifi wifi : wifiList) {

				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, wifi.getWifiNum()); // 파라미터 1부터
				preparedStatement.setString(2, wifi.getGu());
				preparedStatement.setString(3, wifi.getName());
				preparedStatement.setString(4, wifi.getAddress1());
				preparedStatement.setString(5, wifi.getAddress2());
				preparedStatement.setString(6, wifi.getFloor());
				preparedStatement.setString(7, wifi.getType());
				preparedStatement.setString(8, wifi.getOrg());
				preparedStatement.setString(9, wifi.getSvc());
				preparedStatement.setString(10, wifi.getMangType());
				preparedStatement.setString(11, wifi.getInstallYear());
				preparedStatement.setString(12, wifi.getInout());
				preparedStatement.setString(13, wifi.getWifiEnv());
				preparedStatement.setString(14, wifi.getLat());
				preparedStatement.setString(15, wifi.getLnt());
				preparedStatement.setString(16, wifi.getWorkDay());
				
				preparedStatement.executeUpdate();
			}
			preparedStatement.close();
            connection.close();

            System.out.println("저장 성공");
            
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null ) {
                    preparedStatement.close();
                }
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
