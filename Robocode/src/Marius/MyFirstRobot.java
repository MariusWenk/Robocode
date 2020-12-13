package Marius;
import robocode.RobocodeFileWriter;

import java.io.File;
import java.io.IOException;

public class MyFirstRobot extends SimpleRobot {


	public MyFirstRobot() throws IOException {
		behavior=new MyFirstBehavior(this);
	}
}
