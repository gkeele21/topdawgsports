FROM centos

LABEL maintainer="grantkeele@gmail.com"

RUN mkdir /opt/tomcat

WORKDIR /opt/tomcat
RUN curl -O https://www-eu.apache.org/dist/tomcat/tomcat-7/v7.0.99/bin/apache-tomcat-7.0.99.tar.gz
RUN tar xvfz apache*.tar.gz
RUN mv apache-tomcat-7.0.99/* /opt/tomcat/.
RUN yum -y install java
RUN java -version
COPY ["context.xml","/opt/tomcat/conf/"]

COPY out/artifacts/topdawgsports/topdawgsports.war /opt/tomcat/webapps

WORKDIR /opt/tomcat/webapps
#RUN mkdir /opt/tomcat/topdawgsports
#ADD topdawgsports.war /opt/tomcat/webapps
#COPY out/artifacts/topdawgsports/topdawgsports.war /opt/tomcat/webapps
#ADD ROOT.war /opt/tomcat/webapps

EXPOSE 8080

#ENV JAVA_HOME /usr/local/jvm

CMD ["/opt/tomcat/bin/catalina.sh", "run"]
#CMD ["/bin/bash"]
