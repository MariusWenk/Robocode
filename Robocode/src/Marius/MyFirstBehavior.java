package Marius;
import java.awt.Color;

public class MyFirstBehavior extends SimpleRobotBehavior {
	//Ged�chtnissektion

	double angle =  360;
	boolean scanned = false;
	int recordTime = 10;
	double bearing;
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
	double[] headings = new double[recordTime];
	double[] gunHeadings = new double[recordTime];
	Point[] ownPositions = new Point[recordTime];
	Point[] enemyPositions = new Point[recordTime];
	double width;
	double height;




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
		width = getBattleFieldWidth();
		height = getBattleFieldHeight();
	}

	@Override
	void execute() {
		scanner();
		for(int i = recordTime-2; i >=0;i--){
			ownPositions[i+1]=ownPositions[i];
			enemyPositions[i+1]=enemyPositions[i];
		}
		ownPositions[0] = getPoint();
		enemyPositions[0] = getEnemyPoint();
		shoot();
		move();
	}

	public void scanner(){
		for(var e : getScannedRobotEvents()){
			for(int i = recordTime-2; i >=0;i--){
				headings[i+1]=headings[i];
				gunHeadings[i+1]=gunHeadings[i];
			}
			headings[0] = getHeading();
			gunHeadings[0] = getGunHeading();
			//Scannereinstellung
			bearing = e.getBearing();
			double radarHeading = getRadarHeading();
			double primAngle = Utils.normalRelativeAngle(headings[0]-radarHeading+bearing);
			if(primAngle > 0){
				angle = primAngle+20;
			}
			else{
				angle = primAngle-20;
			}
			//Datenübergabe
			enemyDistance = e.getDistance();
			enemyHeading = e.getHeading();
			enemyVelocity = e.getVelocity();
			enemyEnergy = e.getEnergy();
			scanned = true;
		}
		//Scanner
		turnRadar(angle);
	}

	public void shoot(){
		double power = 4;
		double time = enemyDistance/(20-(3*power));
		shootAtPoint(enemyPositions[0],power);
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

	public void shootAtPoint(Point point, double power){
		double distance = Math.sqrt(((point.getX()-ownPositions[0].getX())*(point.getX()-ownPositions[0].getX()))+((point.getY()-ownPositions[0].getY())*(point.getY()-ownPositions[0].getY())));
		double phi = Math.toDegrees(Math.asin((point.getX()-ownPositions[0].getX())/distance));
		if((point.getY()-ownPositions[0].getY()) <= 0) {
			phi = 180 - phi;
		}
		phi = Utils.normalAbsoluteAngle(phi);
		System.out.println(Math.asin(-1));
		shootInRoomAngle(phi, power);
	}

	public double getAbsoluteBearings(double bearing, double heading){
		return Utils.normalAbsoluteAngle(bearing+heading);
	}

	public Point getEnemyPoint(){
		double absoluteEnemyAngle = getAbsoluteBearings(bearing,headings[0]);
		double x = ownPositions[0].getX() + (Math.sin(Math.toRadians(absoluteEnemyAngle)) * enemyDistance);
		double y = ownPositions[0].getY() + (Math.cos(Math.toRadians(absoluteEnemyAngle)) * enemyDistance);
		return new Point(x,y);
	}
}
