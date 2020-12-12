package Marius;
import java.awt.Color;

public class MyFirstBehavior extends SimpleRobotBehavior {
	//Ged�chtnissektion

	double angle =  360;
	boolean scanned = false;
	int recordTime = 6; // recordTime and power at the shoot Method could be strategically varied
	double power = 4;
	double bearing;
	double enemyDistance;
	double enemyEnergy;
	double turned = 0;
	double turn = 5;
	int wallEscapeDuration = 0;
	int robotEscapeDuration = 0;
	double[] headings = new double[recordTime];
	double[] gunHeadings = new double[recordTime];
	double[] enemyVelocitys = new double[recordTime];
	double[] enemyHeadings = new double[recordTime];
	Point[] ownPositions = new Point[recordTime];
	Point[] enemyPositions = new Point[recordTime];
	double width;
	double height;
	int moveTime = 0;
	boolean distanceBool = false;
	int distanceBoolCounter = 0;
	int time = 0;

	public MyFirstBehavior(SimpleRobot  robot) {
		super(robot);
	}

	@Override
	public void start() {
		setColors(new Color(330000),Color.DARK_GRAY, Color.ORANGE,Color.red,Color.green);
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
			enemyPositions[i+1]= enemyPositions[i];
		}
		ownPositions[0] = getPoint();
		enemyPositions[0] = getEnemyPoint();
		shootAverageVelocityAndHeading();
		moveRandomAndEscape();
		time += 1;
		if(getEnergy() < 20){
			power = 2;
			setColors(Color.orange,Color.DARK_GRAY, Color.blue,Color.yellow,Color.red);
		}
	}

	/* Adjustment of Radar and gathering of Data */
	public void scanner(){
		for(var e : getScannedRobotEvents()){
			for(int i = recordTime-2; i >=0;i--){
				headings[i+1]=headings[i];
				gunHeadings[i+1]=gunHeadings[i];
				enemyVelocitys[i+1]=enemyVelocitys[i];
				enemyHeadings[i+1]=enemyHeadings[i];
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
			enemyHeadings[0] = e.getHeading();
			enemyVelocitys[0] = e.getVelocity();
			enemyEnergy = e.getEnergy();
			scanned = true;
		}
		//Scanner
		turnRadar(angle);
	}

	/* Different shoot Strategies */
	public void shootAtActualPosition(){
		shootAtPoint(enemyPositions[0],power);
	}

	public void shootAverageVelocityAndHeading(){
		double averageVelocityChange = 0;
		double averageHeadingChange = 0;
		for(int i = 0; i < Math.min(recordTime,time)-1; i++){
			averageVelocityChange += enemyVelocitys[i]-enemyVelocitys[i+1];
			averageHeadingChange += enemyHeadings[i]-enemyHeadings[i+1];
		}
		averageVelocityChange /= recordTime;
		averageHeadingChange /= recordTime;
		double time = enemyDistance/(20-(3*power));
		double estimatedX = enemyPositions[0].getX();
		double estimatedY = enemyPositions[0].getY();
		for(int i = 0; i<time;i++){
			estimatedX += getPointDifference(averageVelocityChange, averageHeadingChange*i).getX();
			estimatedY += getPointDifference(averageVelocityChange, averageHeadingChange*i).getY();
		}
		shootAtPoint(new Point(estimatedX, estimatedY),power);
	}

	public void shootAverageVelocityAndHeadingWeighted(){
		double[] velocityChange = new double[recordTime-1];
		double[] headingChange = new double[recordTime-1];
		for(int i = 0; i < Math.min(recordTime,time)-1; i++){
			velocityChange[i] = enemyVelocitys[i]-enemyVelocitys[i+1];
			headingChange[i] = enemyHeadings[i]-enemyHeadings[i+1];
		}
		int time = (int)(enemyDistance/(20-(3*power)));
		double estimatedX = enemyPositions[0].getX();
		double estimatedY = enemyPositions[0].getY();
		double[] headChange = new double[time-1];
		double n = 0;
		for(int i = 0; i<time;i++){
			n += i;
		}
		for(int i = 0; i<time;i++){
			headChange[i+1] = headChange[i]+headingChange[i]*((time-i)/n);
			estimatedX += getPointDifference(velocityChange[i], (headingChange[i]*((time-i)/n))+headChange[i]).getX();
			estimatedY += getPointDifference(velocityChange[i], (headingChange[i]*((time-i)/n))+headChange[i]).getY();
		}
		shootAtPoint(new Point(estimatedX, estimatedY),power);
	}

	public void shootAveragePositionDevelopment(){
		double averageXChange = 0;
		double averageYChange = 0;
		for(int i = 0; i < Math.min(recordTime,time)-1; i++){
			averageXChange += enemyPositions[i].getX()- enemyPositions[i+1].getX();
			averageYChange += enemyPositions[i].getY()- enemyPositions[i+1].getY();
		}
		averageXChange /= recordTime;
		averageYChange /= recordTime;
		double time = enemyDistance/(20-(3*power));
		double estimatedX = enemyPositions[0].getX()+averageXChange*time;
		double estimatedY = enemyPositions[0].getY()+averageYChange*time;
		shootAtPoint(new Point(estimatedX, estimatedY),power);
	}

	public void shootPositionHeadingVelocityDevelopment(){

	}

	public void shootPosibleMovePositions(){
		
	}

	/*Different move Strategies */
	public void moveBasic(){
		boolean escapeWall = escapeWall(180,20);
		boolean escapeRobot = false;
		if(!escapeWall){
			escapeRobot = escapeRobot(120,20);
		}
		if(!escapeRobot && !escapeWall){
			ahead(10);
			turn(turn);
			turned+=5;
			if (turned == 180){
				turn = -turn;
				turned = 0;
			}
		}
	}

	public void moveRandomAndEscape(){
		boolean escapeWall = escapeWall(180,20);
		boolean escapeRobot = false;
		if(!escapeWall){
			escapeRobot = escapeRobot(120,20);
		}

		int maxTime = 14;
		if(!escapeRobot && !escapeWall){
			if(moveTime == 0){
				moveTime = (int) (Math.random()*maxTime);
				int randTurn = (int) (Math.random()*360-180);
				turn(randTurn);
			}
			else{
				moveTime--;
			}
			if(distanceBoolCounter == 0){
				distanceBool = false;
				setColors(new Color(330000),Color.DARK_GRAY, Color.ORANGE,Color.red,Color.green);
			}
			else{
				distanceBoolCounter--;
			}
			if(notKeepingRobotDistance(100) && !distanceBool){
				distanceBool = true;
				distanceBoolCounter = 10;
				setColors(new Color(204,0,0),Color.DARK_GRAY, Color.ORANGE,Color.black,Color.green);
				turn(180);
			}
			if(notKeepingWallDistance(100) != 0){
				turn(notKeepingWallDistance(100));
			}
			int randDist = (int) (Math.random()*10-15);
			ahead(randDist);
		}
	}

	/* Help Methods for Shooting */
	public void shootInRoomAngle(double angle,double power){
		double gunAngle = Utils.normalRelativeAngle(angle-gunHeadings[0]);
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

	/* Help Methods for Moving */
	public boolean escapeWall(double angle, int duration){ // angle must be between 90 and 270
		for(var e : getHitWallEvents()){
			double wallAngle = e.getBearing();
			turn(Utils.normalRelativeAngle(wallAngle-angle));
			wallEscapeDuration = duration;
		}
		if(wallEscapeDuration != 0){
			ahead(10);
			wallEscapeDuration--;
			return true;
		}
		else{
			return false;
		}
	}

	public boolean escapeRobot(double angle, int duration){ // angle must be between 90 and 270
		for(var e : getHitRobotEvents()){
			double robotAngle = e.getBearing();
			turn(Utils.normalRelativeAngle(robotAngle-angle));
			robotEscapeDuration = duration;
		}
		if(robotEscapeDuration != 0){
			ahead(10);
			robotEscapeDuration--;
			return true;
		}
		else{
			return false;
		}
	}

	public boolean notKeepingRobotDistance(double distance){
		double heading = getHeading();
		double velocity = getVelocity();
		double newX = ownPositions[0].getX() + getPointDifference(velocity,heading).getX();
		double newY = ownPositions[0].getY() + getPointDifference(velocity,heading).getY();
		double newDistance = Math.sqrt(((newX- enemyPositions[0].getX())*(newX- enemyPositions[0].getX()))+((newY- enemyPositions[0].getY())*(newY- enemyPositions[0].getY())));
		return newDistance <= distance;
	}

	public double notKeepingWallDistance(double distance){
		double heading = getHeading();
		double velocity = getVelocity();
		double newX = ownPositions[0].getX() + getPointDifference(velocity,heading).getX();
		double newY = ownPositions[0].getY() + getPointDifference(velocity,heading).getY();
		if(newY <= distance){
			return Utils.normalRelativeAngle(180-heading);
		}
		else if(height-newY <= distance){
			return Utils.normalRelativeAngle(heading-180);
		}
		else if(newX <= distance){
			return Utils.normalRelativeAngle(heading-90);
		}
		else if(width-newX <= distance){
			return Utils.normalRelativeAngle(90-heading);
		}
		return 0;
	}

	/* Help Methods for Angles */
	public double getAbsoluteBearings(double bearing, double heading){
		return Utils.normalAbsoluteAngle(bearing+heading);
	}

	/* Help Methods for cartesian Coordinates */
	public Point getEnemyPoint(){
		double absoluteEnemyAngle = getAbsoluteBearings(bearing,headings[0]);
		double x = ownPositions[0].getX() + (Math.sin(Math.toRadians(absoluteEnemyAngle)) * enemyDistance);
		double y = ownPositions[0].getY() + (Math.cos(Math.toRadians(absoluteEnemyAngle)) * enemyDistance);
		return new Point(x,y);
	}

	public Point getPointDifference(double distance, double heading){
		double x = (Math.sin(Math.toRadians(heading)) * distance);
		double y = (Math.cos(Math.toRadians(heading)) * distance);
		return new Point(x,y);
	}
}
