package ktime.utils.stopwatch;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 11/3/13
 * Time: 8:41 PM
 * To Change this template use File | Settings | File Templates.
 */
public interface StopwatchListener {
    public void onChanged(Change change);

    public interface Change {
        public boolean isRunning();

        /**
         * Returns the type of Change that was made.
         * @return StopwatchEventType
         */
        public StopwatchEventType getChangeType();

        /**
         * Returns the changed split's index.
         * @return int splitIndex
         */
        public int getChangedSplit();

        /**
         * Return the time for the newly created segment. Null if unable to computer or stopwatch started.
         * @return Long time
         */
        public Long getNewSegmentTime();

        /**
         * Return the timestamp for the added split if one was added. If start/stop return those timestamps.
         * If skipped return null.
         * @return Long timestamp
         */
        public Long getNewSplitTime();
    }

    public enum StopwatchEventType {
        START, STOP, RESET, SPLIT, UNSPLIT, SKIPSPLIT
    }
}
