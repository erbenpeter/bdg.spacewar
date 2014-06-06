package bdg.spacewar;

import java.io.IOException;

public class Test {
	public static void main(String[] args) throws IOException{
		while(true){
			while(System.in.available() == 0);
			while(System.in.available() > 0)
				System.in.read();
			System.out.println("1 0 false");
		}
	}
}
