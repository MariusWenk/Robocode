package Marius;
import java.awt.Color;

public class MyFirstBehavior extends SimpleRobotBehavior {
	//Ged�chtnissektion

	double angle =  360;
	boolean scanned = false;
	double[] bearings = new double[]{0,0};
	double enemyDistance;
	double enemyHeading;
	double enemyVelocity;
	double enemyEnergy;
	double turned = 0;
	double turn = 5;
	boolean escapeWall = false;
	int wallEscapeDuration = 0;
	boolean escapeRobot = false;
	int robotEscapeDuration = 0;
	double[] enemyAngles = new double[bearings.length];
	double[] headings = new double[bearings.length];
	double[] gunHeadings = new double[bearings.length];




	public MyFirstBehavior(SimpleRobot  robot) {
		super(robot);
	}

	@Override
	public void start() {
		turnRadar(360);
		for(var e : getScannedRobotEvents()){
			double turnAngle = Utils.normalRelativeAngle(90-e.getBearing());
			turn(turnAngle);
		}
	}

	@Override
	void execute() {
		scannerShooter();
		move();
	}

	public void scannerShooter(){
		double absoluteShootAngle = 0;
		double power = 4;
		for(var e : getScannedRobotEvents()){
			//Scannereinstellung
			for(int i = 0; i < bearings.length - 1;i++){
				bearings[i+1]=bearings[i];
				headings[i+1]=headings[i];
				gunHeadings[i+1]=gunHeadings[i];
			}
			bearings[0] = e.getBearing();
			headings[0] = getHeading();
			double radarHeading = getRadarHeading();
			double primAngle = Utils.normalRelativeAngle(headings[0]-radarHeading+bearings[0]);
			if(primAngle > 0){
				angle = primAngle+20;
			}
			else{
				angle = primAngle-20;
			}
			//Guneinstellung
			gunHeadings[0] = getGunHeading();
			enemyDistance = e.getDistance();
			enemyHeading = e.getHeading();
			enemyVelocity = e.getVelocity();
			double time = enemyDistance/(20-(3*power));
			double[] absoluteEnemyAngles = getAbsoluteBearings(bearings,headings);
			absoluteShootAngle = absoluteEnemyAngles[0]+((absoluteEnemyAngles[0]-absoluteEnemyAngles[1])*time/8);
			//Datenübergabe
			enemyEnergy = e.getEnergy();
			scanned = true;
		}
		//Scanner
		turnRadar(angle);
		//Gun
		shootInRoomAngle(absoluteShootAngle,power);
	}

	public void move(){
		double wallAngle = 0;
		for(var e : getHitWallEvents()){
			wallAngle = e.getBearing();
			escapeWall = true;
			turn(Utils.normalRelativeAngle(wallAngle-180));
		}
		double robotAngle = 0;
		for(var e : getHitRobotEvents()){
			robotAngle = e.getBearing();
			escapeRobot = true;
			turn(Utils.normalRelativeAngle(robotAngle-180));
		}
		if(escapeWall){
			ahead(10);
			if(wallEscapeDuration == 20){
				escapeWall = false;
				wallEscapeDuration = 0;
			}
			wallEscapeDuration++;
		}
		else if(escapeRobot){
			ahead(10);
			if(robotEscapeDuration == 20){
				escapeWall = false;
				robotEscapeDuration = 0;
			}
			robotEscapeDuration++;
		}
		else{
			ahead(10);
			turn(turn);
			turned+=5;
			if (turned == 180){
				turn = -turn;
				turned = 0;
			}
		}
	}

	public void shootInRoomAngle(double angle,double power){
		double gunAngle = 0;
		gunAngle = Utils.normalRelativeAngle(angle-gunHeadings[0]);
		turnGun(gunAngle);
		if(Math.abs(gunHeadings[0]-angle)<=2){
			fireBullet(power);
		}
	}

	public double[] getAbsoluteBearings(double[] bearings, double[] headings){
		double[] absoluteBearings = new double[bearings.length];
		for(int i = 0;i< bearings.length;i++){
			absoluteBearings[i] = Utils.normalAbsoluteAngle(bearings[i]+headings[i]);
		}
		return absoluteBearings;
	}

}
