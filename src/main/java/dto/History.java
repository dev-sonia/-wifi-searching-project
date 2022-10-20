package dto;

public class History {
	private int id; // 오름차순 증가 id
	private String xCoordinate; // x좌표
	private String yCoordinate; // y좌표
	private String writeDate; // 기록날짜(타임스탬프)
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getxCoordinate() {
		return xCoordinate;
	}
	public void setxCoordinate(String xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	
	public String getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(String yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	
	public String getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
}
