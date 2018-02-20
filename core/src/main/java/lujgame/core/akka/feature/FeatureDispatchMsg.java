package lujgame.core.akka.feature;

public class FeatureDispatchMsg {

  public FeatureDispatchMsg(ActorFeature featureId, Object featureMsg) {
    _featureId = featureId;
    _featureMsg = featureMsg;
  }

  public ActorFeature getFeatureId() {
    return _featureId;
  }

  public Object getFeatureMsg() {
    return _featureMsg;
  }

  private final ActorFeature _featureId;

  private final Object _featureMsg;
}
