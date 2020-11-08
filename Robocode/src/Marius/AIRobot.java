package Marius;

public class AIRobot extends SimpleRobot {
    public AIRobot() {
        behavior=new AIRobotBehavior(this);
    }
}
