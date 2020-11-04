package infovk.USERNAME;
import java.awt.Color;

public class MyFirstBehavior extends SimpleRobotBehavior {
	//Gedächtnissektion
	
	public MyFirstBehavior(SimpleRobot  robot) {
		super(robot);
	}
	
	@Override
	public void start() {
		turnRadar(720);
	}

	@Override
	void execute() {
		ahead(10);
	}
	
	//Eigene Funktionen Sektion
}
