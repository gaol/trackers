Trackers
===========

Trackers project aims to track all information of EAP and layered products like:

   * Jar information
      * groupId, artifactId, version of each jar
      * which package does this jar belongs to
      * Which build produces this jar

   * Product Information
      * Which build produces this product(including the full build command line)
      * How many versions for a specific product
      * What jars each product version has

   * CVE issues
      * Which CVEs affect our products
      * Which jars does a specfic CVE affect
      * In which jar version does this CVE is fixed

Test
=====
Run:

>> mvn -Parq-jbossas-managed clean test

>> NOTE: it needs you set up $jbossHome environment, '/softwares/eap6/fortest' is used by default.

Build
=====

Run:

>> mvn -Dmaven.test.skip=true clean install


