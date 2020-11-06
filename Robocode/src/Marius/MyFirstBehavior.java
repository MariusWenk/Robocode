package Marius;
import java.awt.Color;

public class MyFirstBehavior extends SimpleRobotBehavior {
	//Ged�chtnissektion

	double angle =  360;
	boolean scanned = false;
	double bearing;
	double enemyDistance;
	double enemyHeading;
	double enemyVelocity;
	double enemyEnergy;

	public MyFirstBehavior(SimpleRobot  robot) {
		super(robot);
	}

	@Override
	public void start() {
		turnRadar(360);
	}

	@Override
	void execute() {
		scannerShooter();
		move();
		//ahead(10);
	}

	public void scannerShooter(){
		double gunAngle = 0;
		double power = 1;
		for(var e : getScannedRobotEvents()){
			//Scannereinstellung
			bearing = e.getBearing();
			double heading = getHeading();
			double radarHeading = getRadarHeading();
			double primAngle = Utils.normalRelativeAngle(heading-radarHeading+bearing);
			if(primAngle > 0){
				angle = primAngle+20;
			}
			else{
				angle = primAngle-20;
			}
			//Guneinstellung
			double gunHeading = getGunHeading();
			enemyDistance = e.getDistance();
			enemyHeading = e.getHeading();
			enemyVelocity = e.getVelocity();
			gunAngle = Utils.normalRelativeAngle(heading-gunHeading+bearing);
			//Datenübergabe
			enemyEnergy = e.getEnergy();
			scanned = true;
		}
		//Scanner
		turnRadar(angle);
		//Gun
		turnGun(gunAngle);
		if(gunAngle <=2 && gunAngle >= -2){
			fireBullet(power);
		}
	}

	public void move(){

	}

}
