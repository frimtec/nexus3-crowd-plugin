FROM sonatype/nexus3:3.76.1

USER root

# Add nexus-crowd-plugin
COPY etc/crowd.properties /opt/sonatype/nexus/etc/crowd.properties
COPY target/nexus3-crowd-plugin-0-SNAPSHOT-bundle.kar /opt/sonatype/nexus/deploy

# setup permissions
RUN chown nexus:nexus -R /opt/sonatype/nexus

USER nexus