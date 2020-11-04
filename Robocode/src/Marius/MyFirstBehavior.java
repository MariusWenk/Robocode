package Marius;
import java.awt.Color;

public class MyFirstBehavior extends SimpleRobotBehavior {
	//Ged�chtnissektion
	
	public MyFirstBehavior(SimpleRobot  robot) {
		super(robot);
	}
	
	@Override
	public void start() {
		turnRadar(720);
	}

	@Override
	void execute() {
		for(var e : getScannedRobotEvents()){

		}
		ahead(10);
	}
	
	//Eigene Funktionen Sektion
}
