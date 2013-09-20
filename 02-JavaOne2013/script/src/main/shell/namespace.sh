DUKE_IMG_HOME=/Users/antoniombp/Documents/Code/github/agoncal-sample-javaee/02-JavaOne2013/application/src/main/webapp/resources

APP_HOME=/Users/antoniombp/Documents/Code/javaone/javaone-javaee7
APP_JAVA=$APP_HOME/src/main/java/org/javaone/javaee7
APP_RESOURCES=$APP_HOME/src/main/resources/META-INF
APP_WEBAPP=$APP_HOME/src/main/webapp

SNAP1_HOME=/Users/antoniombp/Documents/Code/javaone/snapshots/javaone-javaee7-1
SNAP1_JAVA=$SNAP1_HOME/src/main/java/org/javaone/javaee7
SNAP1_RESOURCES=$SNAP1_HOME/src/main/resources/META-INF
SNAP1_WEBAPP=$SNAP1_HOME/src/main/webapp

cp $SNAP1_RESOURCES/persistence.xml $APP_RESOURCES/persistence.xml
cp $SNAP1_RESOURCES/validation.xml $APP_RESOURCES/validation.xml

cp $SNAP1_WEBAPP/resources/scaffold/pageTemplate.xhtml $APP_WEBAPP/resources/scaffold/pageTemplate.xhtml
cp $SNAP1_WEBAPP/resources/scaffold/paginator.xhtml $APP_WEBAPP/resources/scaffold/paginator.xhtml

cp $SNAP1_WEBAPP/book/create.xhtml $APP_WEBAPP/book/create.xhtml
cp $SNAP1_WEBAPP/book/search.xhtml $APP_WEBAPP/book/search.xhtml
cp $SNAP1_WEBAPP/book/view.xhtml $APP_WEBAPP/book/view.xhtml

cp $SNAP1_WEBAPP/speaker/create.xhtml $APP_WEBAPP/speaker/create.xhtml
cp $SNAP1_WEBAPP/speaker/search.xhtml $APP_WEBAPP/speaker/search.xhtml
cp $SNAP1_WEBAPP/speaker/view.xhtml $APP_WEBAPP/speaker/view.xhtml

cp $SNAP1_WEBAPP/talk/create.xhtml $APP_WEBAPP/talk/create.xhtml
cp $SNAP1_WEBAPP/talk/search.xhtml $APP_WEBAPP/talk/search.xhtml
cp $SNAP1_WEBAPP/talk/view.xhtml $APP_WEBAPP/talk/view.xhtml

cp $SNAP1_WEBAPP/WEB-INF/beans.xml $APP_WEBAPP/WEB-INF/beans.xml
cp $SNAP1_WEBAPP/WEB-INF/faces-config.xml $APP_WEBAPP/WEB-INF/faces-config.xml
cp $SNAP1_WEBAPP/WEB-INF/web.xml $APP_WEBAPP/WEB-INF/web.xml

cp $SNAP1_WEBAPP/error.xhtml $APP_WEBAPP/error.xhtml
cp $SNAP1_WEBAPP/index.xhtml $APP_WEBAPP/index.xhtml

cp $SNAP1_JAVA/view/BookBean.java $APP_JAVA/view/BookBean.java
cp $SNAP1_JAVA/view/SpeakerBean.java  $APP_JAVA/view/SpeakerBean.java
cp $SNAP1_JAVA/view/TalkBean.java  $APP_JAVA/view/TalkBean.java

cp $DUKE_IMG_HOME/duke-javaee7* $APP_WEBAPP/resources/
cp $DUKE_IMG_HOME/duke-javaee7* $SNAP1_WEBAPP/resources/