import java.io.Serializable;
import java.nio.channels.Pipe;

public class Packet implements Serializable {
	private long useID;
	private long videoID;
	private long storageID;
	private long size;
	private String type;
	private byte[] data;
	private String ip;
	private String port;
	private String address;
	
	public Packet(long id1, long id2, long id3, long size, String type, byte[] instream, String ip, String port,String address) {
		// TODO Auto-generated constructor stub
		this.useID = id1;
		this.videoID = id2;
		this.storageID = id3;
		this.size = size;
		this.type = type;
		this.data = instream;
		this.ip = ip;
		this.port = port;
		this.address = address;
	}
	
	public long GetUseId() {
		return useID;
	}
	
	public long GetVideoId() {
		return videoID;
	}
	
	public long GetSize() {
		return size;
	}
	
	public long GetStorageId() {
		return storageID;
	}
	
	public String GetType() {
		return type;
	}
	
	public byte[] GetData() {
		return data;
	}
	
	public String GetIp() {
		return ip;
	}
	
	public String GetPort() {
		return port;
	}
	
	public String GetAddress() {
		return address;
	}
}
