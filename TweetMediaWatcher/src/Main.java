import java.io.IOException;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;


public class Main {
	public static void main(String[] args) throws TwitterException, IOException {
		Browser browser = new Browser();
		StatusListener listener = new StatusListener() {
			
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
			
			@Override
			public void onStatus(Status status) {
				// URLを取ってきて強制的にブラウザで開く
				URLEntity[] urlArray = status.getURLEntities();
				
				int size = urlArray.length;
				for (int i=0; i<size; i++) {
					String url = urlArray[i].getExpandedURL().toString();
					if (url.indexOf("nicovideo") != -1) { // ニコニコ動画
						browser.open(url);
					} else if (url.indexOf("soundcloud") != -1) { // SoundCloud
						browser.open(url);
					} else if (url.indexOf("tmbox") != -1) { // tmbox
						browser.open(url);
					}
				}
			}
			
			public void onStallWarning(StallWarning arg0) {}
			public void onScrubGeo(long arg0, long arg1) {}
			public void onDeletionNotice(StatusDeletionNotice arg0) {}
		};
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(listener);
		twitterStream.user();
	}

}
