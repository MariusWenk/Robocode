package Marius;
import java.awt.Color;

public class AIRobotBehavior extends SimpleRobotBehavior{

    double angle = 0;
    double[] bearings = new double[10];
    double[] headings = new double[bearings.length];
    double[] gunHeadings = new double[bearings.length];
    double enemyDistance;
    double enemyHeading;
    double enemyVelocity;
    double enemyEnergy;
    String enemyName;

    double xPosition;
    double yPosition;
    Point pointPosition;
    double battlefieldWidth;
    double battlefieldHeight;

    double turnRemaining;
    double velocity;
    double distanceRemaining;

    double gunTurnRemaining;
    double gunCoolingRate;
    double gunHeat;

    double time;
    double energy;


    AIRobotBehavior(SimpleRobot robot) {
        super(robot);
    }

    @Override
    void start() {
        for (int i = 0;i<bearings.length;i++){
            bearings[i] = 0;
            headings[i] = 0;
            gunHeadings[i] = 0;
        }
        turnRadar(360);
        for(var e : getScannedRobotEvents()){
            double turnAngle = Utils.normalRelativeAngle(90-e.getBearing());
            turn(turnAngle);
        }
    }

    @Override
    void execute() {
        scanAllInformation();
    }

    public void scanAllInformation(){
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
            enemyDistance = e.getDistance();
            enemyHeading = e.getHeading();
            enemyVelocity = e.getVelocity();
            enemyEnergy = e.getEnergy();
            enemyName = e.getName();
        }
//        for(var e : getBulletHitBulletEvents()){
//            e.getBullet();
//            e.getHitBullet();
//        }
//        for(var e : getBulletHitEvents()){
//            e.getBullet();
//            e.getEnergy();
//            e.getName();
//        }
//        for(var e : getBulletMissedEvents()){
//            e.getBullet();
//        }
//        for(var e : getHitByBulletEvents()){
//            e.getBearing();
//            e.getBullet();
//            e.getHeading();
//            e.getName();
//            e.getPower();
//            e.getVelocity();
//        }
//        for(var e : getHitRobotEvents()){
//            e.getBearing();
//            e.getEnergy();
//            e.getName();
//            e.isMyFault();
//        }
//        for(var e : getHitWallEvents()){
//            e.getBearing();
//        }
        xPosition = getX();
        yPosition = getY();
        pointPosition = getPoint();
        battlefieldWidth = getBattleFieldWidth();
        battlefieldHeight = getBattleFieldHeight();

        turnRemaining = getTurnRemaining();
        velocity = getVelocity();
        distanceRemaining = getDistanceRemaining();

//        getRadarTurnRemaining();

        gunTurnRemaining = getGunTurnRemaining();
        gunCoolingRate = getGunCoolingRate();
        gunHeat = getGunHeat();

        time = getTime();
        energy = getEnergy();
    }
}
