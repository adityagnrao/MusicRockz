import java.util.*;
import java.io.*;
import java.math.BigInteger;

public class ZipfsSong1 {

	static long n;
	static long m;
	static ArrayList<Songs> song = new ArrayList<Songs>();

	public static void main(String arg[]) throws IOException {
		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(is);
		StringTokenizer st;
		String line = "";
//reads the first line to get n and m
		if ((line = br.readLine()) != null) {
			st = new StringTokenizer(line);
			while (st.hasMoreTokens()) {
				n = Long.parseLong(st.nextToken());
				m = Long.parseLong(st.nextToken());
			}
		}
//starts reading n lines from console		
		for ( Long i = (long) 0; (i < n) && ((line = br.readLine()) != null); i++) {
			st = new StringTokenizer(line);

			while (st.hasMoreTokens()) {
//calculates qi=fi/zi and adds it along with the song title			
				song.add(new Songs(new BigInteger(Long.toString(i + 1)).multiply(new BigInteger(st.nextToken())),st.nextToken(), i));                                     

			}

		}



//sorts the list based on qi
		Collections.sort(song);

        System.out.println("Output:");
//displays top m best quality songs in decreasing order of their quality		
		for (int i = 0; i < m; i++)
			System.out.println(song.get(i).getSong_title());

	}
}

class Songs implements Comparable<Songs> {
	private String song_title;
	private Long index;

	public Songs(BigInteger l, String nextToken2,Long ind) {
		// TODO Auto-generated constructor stub
		this.index=ind;
		this.fi = l;
		this.song_title = nextToken2;
	}

	public String getSong_title() {
		return song_title;
	}

	public void setSong_title(String song_title) {
		this.song_title = song_title;
	}

	public BigInteger getFi() {
		return fi;
	}

	public void setFi(BigInteger fi) {
		this.fi = fi;
	}

	BigInteger fi;

	@Override
	public int compareTo(Songs o) {
		// TODO Auto-generated method stub
		
		if((this.fi).compareTo(o.fi)==0)
				return Long.valueOf(this.index).compareTo(o.index);

		return (o.fi).compareTo(this.fi);
	}

}
