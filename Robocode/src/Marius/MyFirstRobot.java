package Marius;
import robocode.RobocodeFileWriter;

import java.io.File;
import java.io.IOException;

public class MyFirstRobot extends SimpleRobot {

	RobocodeFileWriter rfwriter;

	public MyFirstRobot() throws IOException {
		behavior=new MyFirstBehavior(this);
		getDataDirectory();
		File AIData = getDataFile("AIData.txt");
		rfwriter = new RobocodeFileWriter(AIData);
	}

	public RobocodeFileWriter getWriter(){
		return rfwriter;
	}
}
