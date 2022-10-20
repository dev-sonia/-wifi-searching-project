package service;

import dto.History;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryService {
	
	private static final String JDBC = "org.sqlite.JDBC";
	private static final String URL = "jdbc:sqlite:D:\\BOOTCAMP\\project\\wifi-searching-project\\src\\main\\database\\test.db";

	// saveHistory - 검색 기록 저장 함수
	public void saveHistory(String latitude, String longitude) {
		
		// 현재 날짜/시간        
		LocalDateTime now = LocalDateTime.now();
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
		String formatedNow = now.toString();
	
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
			
			String sql = " insert  into history (xCoord, yCoord, dateWrite) "
                    + "values (?, ?, ?); ";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, latitude);
			preparedStatement.setString(2, longitude);
			preparedStatement.setString(3, formatedNow);
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
            connection.close();
			
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
	
	// cntHistory - 히즈토리 크기 계산 함수
	public int cntHistory() {
		int cnt = 0;
		
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
			
			String sql = " select count(*) from history; ";
			preparedStatement = connection.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			cnt = rs.getInt(1);
			
			preparedStatement.close();
            connection.close();
            
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
                if ( rs != null && !rs.isClosed() ){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
			
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
		return cnt;
	}

	// showHistory - 검색 저장 기록 보여주는 함수
	public List<History> showHistory(int cnt) {
		List<History> historyList = new ArrayList<>();

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

			// select 조건문에 거리 연산 넣기
			String sql = " select xCoord, yCoord, dateWrite from history order by dateWrite DESC; ";

			preparedStatement = connection.prepareStatement(sql);
			rs = preparedStatement.executeQuery();

			while(rs.next()) {
				History history = new History();
				history.setId(cnt);
				history.setxCoordinate(rs.getString("xCoord"));
				history.setyCoordinate(rs.getString("yCoord"));
				history.setWriteDate(rs.getString("dateWrite"));

				historyList.add(history);
				cnt--;
			}
		} catch (SQLException e) {
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
		return historyList;
	}

	// removeHistory - 검색 기록 삭제 함수
	public void removeHistory(String history) {

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
			
			String sql = " delete from history "
						+ " where dateWrite = ? " ;

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, history);

			preparedStatement.executeUpdate();
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
	}
}
