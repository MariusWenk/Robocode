package Marius;
import robocode.AdvancedRobot;
import robocode.RobocodeFileWriter;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MyFirstBehavior extends SimpleRobotBehavior {
	//Ged�chtnissektion

	double angle =  360;
	boolean scanned = false;
	int recordTime = 6; // recordTime and power at the shoot Method could be strategically varied
	double power = 2;
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
	int randStopTime = 0;
	int stopTime = 0;
	double[] AIValues = new double[]{6,4,14,20,100,10,15,100,100};
	int i = 4;
	int h = 5;
	int o = 1;
	NeuralNetwork nn = new NeuralNetwork(i,h,o);
	int shootTime = 0;
	double shoot = 0;
	double[][] AIData_weights_ih = new double[h][i];
	double[][] AIData_weights_ho = new double[o][h];
	double[][] AIData_bias_h = new double[h][1];
	double[][] AIData_bias_o = new double[o][1];
	int bulletDataRecordTime = 10;
	double[][] bulletData = new double[bulletDataRecordTime][i];
	Bullet[] bulletArray = new Bullet[bulletDataRecordTime];
	double[] shootAngleData = new double[bulletDataRecordTime];
	int bulletRecord = 0;
	int modeIndicator = 1;


	public MyFirstBehavior(SimpleRobot  robot) {
		super(robot);
	}

	@Override
	public void start(){
		//saveAIData();
		loadAIData();
		randStopTime = (int) (Math.random()*20);
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
		if(modeIndicator == 0){
			trainAI();
		}
		if(modeIndicator == 1){
			shootAI();
		}
		moveRandomAndEscape();
		time += 1;
		if(time>100){
			recordTime = (int) (AIValues[0]);
			power = (int) (AIValues[1]);
		}
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

	public void trainAI(){
		if(shootTime == 0){
			Point point = enemyPositions[0];
			double distance = Math.sqrt(((point.getX()-ownPositions[0].getX())*(point.getX()-ownPositions[0].getX()))+((point.getY()-ownPositions[0].getY())*(point.getY()-ownPositions[0].getY())));
			double phi = Math.toDegrees(Math.asin((point.getX()-ownPositions[0].getX())/distance));
			if((point.getY()-ownPositions[0].getY()) <= 0) {
				phi = 180 - phi;
			}
			phi = Utils.normalAbsoluteAngle(phi);
			shoot = phi + Math.random()*40;
			shootTime = 10;
		}
		else{
			shootTime--;
		}
		shootInRoomAngle(shoot,power);
		for(var e: getBulletHitEvents()){
			int n = 0;
			for(int i = 0; i<Math.min(bulletDataRecordTime,bulletRecord);i++){
				if(e.getBullet().equals(bulletArray[i])){
					n = i;
				}
			}
			double[][] fitX = new double[][]{bulletData[n]};
			double[] shootAngle = new double[]{shootAngleData[n]/360};
			double[][] fitY = new double[][]{shootAngle};
			nn.fit(fitX,fitY,50);
			saveAIData();
		}
	}

	public void shootAI() {
		double[] enemyData = new double[]{enemyPositions[0].getX()-ownPositions[0].getX(),enemyPositions[0].getY()-ownPositions[0].getY(),enemyHeadings[0],enemyVelocitys[0]};
		if(shootTime == 0){
			shoot = nn.predict(enemyData).get(0)*360;
			shootTime = 10;
		}
		else{
			shootTime--;
		}
		shootInRoomAngle(shoot,power);
	}

	public void shootPossibleMovePositions(){

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

		int maxTime = (int)(AIValues[2]); //14
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
			if(notKeepingRobotDistance(AIValues[7]) && !distanceBool){ //100
				distanceBool = true;
				distanceBoolCounter = 10;
				setColors(new Color(204,0,0),Color.DARK_GRAY, Color.ORANGE,Color.black,Color.green);
				turn(180);
				randStopTime = 4;
			}
			if(notKeepingWallDistance(AIValues[8]) != 0){ //100
				turn(notKeepingWallDistance(AIValues[8])); //100
			}
			randStopTime = 4; // Funktion wird nicht genutzt
			if(randStopTime == 0){
				if(stopTime == 0){
					stopTime = (int) (Math.random()*AIValues[3]+1); //20
				}
				else if(stopTime == 1){
					randStopTime = (int) (Math.random()*AIValues[4]); //100
					stopTime = 0;
				}
				else{
					stopTime--;
				}
				ahead(0);
			}
			else{
				randStopTime--;
				int randDist = (int) (Math.random()*AIValues[5]-AIValues[6]); //10, 15
				ahead(randDist);
			}
		}
	}

	/* Help Methods for Shooting */
	public void shootInRoomAngle(double angle,double power){
		double gunAngle = Utils.normalRelativeAngle(angle-gunHeadings[0]);
		turnGun(gunAngle);
		if(Math.abs(gunHeadings[0]-angle)<=2){
			Bullet bullet = fireBullet(power);
			if(modeIndicator == 0){
				bulletRecord++;
				for(int i = bulletDataRecordTime-2; i >=0;i--){
					bulletData[i+1]=bulletData[i];
					bulletArray[i+1]=bulletArray[i];
					shootAngleData[i+1]=shootAngleData[i];
				}
				bulletData[0] = new double[]{enemyPositions[0].getX()-ownPositions[0].getX(),enemyPositions[0].getY()-ownPositions[0].getY(),enemyHeadings[0],enemyVelocitys[0]};
				bulletArray[0] = bullet;
				shootAngleData[0] = angle;
			}
		}
	}

	public void shootAtPoint(Point point, double power){
		double distance = Math.sqrt(((point.getX()-ownPositions[0].getX())*(point.getX()-ownPositions[0].getX()))+((point.getY()-ownPositions[0].getY())*(point.getY()-ownPositions[0].getY())));
		double phi = Math.toDegrees(Math.asin((point.getX()-ownPositions[0].getX())/distance));
		if((point.getY()-ownPositions[0].getY()) <= 0) {
			phi = 180 - phi;
		}
		phi = Utils.normalAbsoluteAngle(phi);
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

	/* AI Informationen laden */
	public void loadAIData() {
		AIData_weights_ih = loadData(h,i,"AIData_weights_ih.txt");
		AIData_weights_ho = loadData(o,h,"AIData_weights_ho.txt");
		AIData_bias_h = loadData(h,1,"AIData_bias_h.txt");
		AIData_bias_o = loadData(o,1,"AIData_bias_o.txt");
		nn.setWeights_ih(new Matrix(AIData_weights_ih));
		nn.setWeights_ho(new Matrix(AIData_weights_ho));
		nn.setBias_h(new Matrix(AIData_bias_h));
		nn.setBias_o(new Matrix(AIData_bias_o));
	}

	public double[][] loadData(int a, int b, String filename) {
		String AIDataUnsplit[] = new String[a];
		double[][] dataFile = new double[a][b];

		File AIDataFile = robot.getDataFile(filename);

		if (!AIDataFile.canRead() || !AIDataFile.isFile())
			System.exit(0);

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(AIDataFile));
			String zeile = null;
			int t = 0;
			while ((zeile = in.readLine()) != null) {
				AIDataUnsplit[t] =  zeile;
				t++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
		String[][] AIDataString = new String[a][b];
		for(int t = 0; t<a; t++){
			AIDataString[t] = AIDataUnsplit[t].split(";");
			for(int j = 0; j<b; j++){
				dataFile[t][j] = Double.parseDouble(AIDataString[t][j]);
			}
		}
		return dataFile;
	}

	public void saveAIData() {
//		try {
//			File myObj = new File("AIData.txt");
//			if (myObj.createNewFile()) {
//				//System.out.println("File created: " + myObj.getName());
//			} else {
//				//System.out.println("File already exists.");
//			}
//		} catch (IOException e) {
//			System.out.println("An error occurred.");
//			e.printStackTrace();
//		}
		try {
			robot.getDataDirectory();
			File AIDataFile_weights_ih = robot.getDataFile("AIData_weights_ih.txt");
			File AIDataFile_weights_ho = robot.getDataFile("AIData_weights_ho.txt");
			File AIDataFile_bias_h = robot.getDataFile("AIData_bias_h.txt");
			File AIDataFile_bias_o = robot.getDataFile("AIData_bias_o.txt");
			RobocodeFileWriter writer_weights_ih = new RobocodeFileWriter(AIDataFile_weights_ih);
			RobocodeFileWriter writer_weights_ho = new RobocodeFileWriter(AIDataFile_weights_ho);
			RobocodeFileWriter writer_bias_h = new RobocodeFileWriter(AIDataFile_bias_h);
			RobocodeFileWriter writer_bias_o = new RobocodeFileWriter(AIDataFile_bias_o);
			String[] zeile_weights_ih = new String[h];
			String[] zeile_weights_ho = new String[o];
			String[] zeile_bias_h = new String[h];
			String[] zeile_bias_o = new String[o];
			for(int t = 0; t<this.h; t++){
				zeile_weights_ih[t] = Double.toString(nn.getWeights_ih().data[t][0]);
				for(int j = 1; j<this.i; j++){
					zeile_weights_ih[t] = zeile_weights_ih[t]+";"+nn.getWeights_ih().data[t][j];
				}
			}
			for(int t = 0; t<this.h; t++){
				writer_weights_ih.write(zeile_weights_ih[t]);
				if(t != this.h - 1){
					writer_weights_ih.write("\n");
				}
			}

			for(int t = 0; t<this.o; t++){
				zeile_weights_ho[t] = Double.toString(nn.getWeights_ho().data[t][0]);
				for(int j = 1; j<this.h; j++){
					zeile_weights_ho[t] = zeile_weights_ho[t]+";"+nn.getWeights_ho().data[t][j];
				}
			}
			for(int t = 0; t<this.o; t++){
				writer_weights_ho.write(zeile_weights_ho[t]);
				if(t != this.o - 1){
					writer_weights_ho.write("\n");
				}
			}

			for(int t = 0; t<this.h; t++){
				zeile_bias_h[t] = Double.toString(nn.getBias_h().data[t][0]);
			}
			for(int t = 0; t<this.h; t++){
				writer_bias_h.write(zeile_bias_h[t]);
				if(t != this.h - 1){
					writer_bias_h.write("\n");
				}
			}

			for(int t = 0; t<this.o; t++){
				zeile_bias_o[t] = Double.toString(nn.getBias_o().data[t][0]);
			}
			for(int t = 0; t<this.o; t++){
				writer_bias_o.write(zeile_bias_o[t]);
				if(t != this.o - 1){
					writer_bias_o.write("\n");
				}
			}
			writer_weights_ih.close();
			writer_weights_ho.close();
			writer_bias_h.close();
			writer_bias_o.close();
			//System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
