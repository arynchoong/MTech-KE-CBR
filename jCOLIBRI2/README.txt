README v2.1, 2008/01/17

jCOLIBRI is a framework in Java for building Case-based Reasoning (CBR) 
systems. It includes mechanisms to Retrieve, Reuse, Revise and Retain
cases and is designed to be easily extended with new components. 

To find out more about jCOLIBRI, visit 
http://gaia.fdi.ucm.es/projects/jcolibri/


== Getting Started ==

Make sure that Java (1.6 or later) is installed on your computer.

After unzipping the jCOLIBRI zipped file in any directory, run the application
tester that should be in the directory where jCOLIBRI was unzipped.
- In Windows: jCOLIBRI2-Tester.bat
- In UNIX: jCOLIBRI2-Tester.sh

The tester lets you run every test application included in the release 
to exemplify the main features of jCOLIBRI. In the Tester you can access
to the javadocs of the tests which are the main source of documentation.

There is also an example of using the jCOLIBRI jar library in a stand-alone
application named "Travel Recommender". It can be launched using the 
following scripts:
- In Windows: TravelRecommender.bat
- In UNIX: TravelRecommender.sh

== Important files and directories ==

./jCOLIBRI2-Tester.bat : The tester application for Windows.
./jCOLIBRI2-Tester.sh : The tester application for Unix.
./README.txt : This file.
./.project : Eclipse project file that can be imported into that IDE.
./.classpath : Eclipse classpath file with the libraries used in jCOLIBRI.
./build.xml : Apache Ant build file to compile the sources.
./jcolibri2.LICENSE : License agreement.
./src/jcolibri : The souce code of the jCOLIBRI package.
./doc/api/index.html : The index to the javadoc of the jCOLIBRI package.
./doc/uml/ : UML class and sequence diagrams.
./doc/configfilesSchemas/ : XML schemas for the configuration files of the
                            connectors used to store persistent data
./lib : jCOLIBRI as well as third-party libraries included in this release.
./lib/jcolibri2.jar : Compiled jCOLIBRI jar file.
./TravelRecommender : Folder for the Travel Recommender example.
./TravelRecommender.bat : The Travel Recommender application for Windows.
./TravelRecommender.sh : The Travel Recommender application for Unix.

== Including jCOLIBRI in your application ==

Any application using jCOLIBRI should have access to ./lib/jcolibri2.jar 
along with all the jar files in ./lib/commons by including them in your
CLASSPATH or directly in the JVM invocation:

java --classpath COLIBRI_HOME/lib/jcolibri2.jar;
COLIBRI_HOME/lib/commons/antlr-2.7.6.jar;
COLIBRI_HOME/lib/commons/commons-collections-3.2.jar;
COLIBRI_HOME/lib/commons/commons-logging-1.1.jar;
COLIBRI_HOME/lib/commons/dom4j-1.6.1.jar;
COLIBRI_HOME/lib/commons/junit.jar;
COLIBRI_HOME/lib/commons/log4j-1.2.14.jar;
COLIBRI_HOME/lib/commons/stax-api-1.0.1.jar;
<your.main.class>

Additional libraries should be included depending on the features of the 
CBR application being developed:

./lib/databaseconnector : Database connection.
./lib/ontobridge : Ontology management. 
./lib/textual : Textual CBR extension. 
./lib/evaluation : CBR system evaluation. 
./lib/visualization : Case Base visualization.

To facilitate the inclusion of libraries without polluting the CLASSPATH nor
writing extremely long command lines, jCOLIBRI includes a launcher class that
loads all the libraries at runtime reading the configuration from an XML file
using the DTD of a .classpath file in Eclipse. 
The main method of the jcolibri.util.Launcher class takes as first argument 
the main class of your CBR application and as second argument the file that 
stores the location of the libraries. This second argument is optional, so if 
not provided the launcher looks for a file named .classpath in the current 
directory. For example, the application tester can be invoked from the jCOLIBRI
installation directory with:

java -cp lib/jcolibri2.jar jcolibri.util.Launcher jcolibri.test.main.MainTester

or, from any directory with:

java -cp COLIBRI_HOME/lib/jcolibri2.jar 
jcolibri.util.Launcher jcolibri.test.main.MainTester COLIBRI_HOME/.classpath


== Extending jCOLIBRI ==

Import ./.project into Eclipse or use your favorite IDE to configure a
project with the source files in ./src/jcolibri including all the libraries
specified in ./.classpath and enjoy. We will be happy to consider any extension 
that you develop for inclusion in the main distribution.


== Generating binaries and documentation ==

./build.xml is an Apache Ant build file that compiles the sources in ./src 
into ./bin, overwrites ./lib/jcolibri2.jar and generates the javadoc files
into ./doc. 
With Apache Ant installed in your computer, invoke it from the jCOLIBRI 
installation directory with:

"ant" or "ant all"
(this will compile, create ./lib/jcolibri2.jar, and generate the javadoc)

if you want to generate only some elements use the following ant targets:

"ant build"   -> compiles the sources in ./src into ./bin
"ant jar"     -> creates the ./lib/jcolibri2.jar library
"ant javadoc" -> generates the javadoc files into ./doc
"ant clean"   -> cleans the project


== Release notes ==

jCOLIBRI 2 is a major release where most of the jCOLIBRI 1.x framework
has been reimplemented. The aim of this new version is to improve the
architecture of the framework making it more robust and easier to extend.
However, jCOLIBRI 2.1 does not include the authoring tools that in
version 1.x allow to build a CBR system without writing a single line of 
code. Authoring tools are under development for jCOLIBRI 2.x but, in the 
meantime, version 1.1 can be a better solution for those users that don't
want to write Java code.

== What's new in Version 2.1 ==

Version 2.1 includes methods for developing recommendation systems. 
Some of the implemented methods can be used in general CBR 
applications and other are specific for recommender systems.

General CBR methods:

-- Filtering Retrieval method.
-- XML utils to serialize cases and queries.
-- Methods to obtain the query graphically (using forms).
-- Methods to display cases.
-- Cases retrieval using diversity.
-- Cases selection using diversity.

Specific methods for recommendation systems:

-- Methods to implement the Expert Clerk recommender.
-- Methods to implement collaborative recommenders.
-- Methods to obtain the query from user profiles.
-- Methods to implement Navigation by Asking recommenders.
-- Methods to implement Navigation by Proposing recommenders.
-- Local similarity measures for recommender systems.

jCOLIBRI 2.1 includes 14 new examples that implement different
recommenders.

== Changes from Version 2.0 to Version 2.1 ==

There is an important change in the Nearest Neighbour retrieval package.
In version 2.0 the KNNRetreivalMethod receives a KNNConfig object that
contains the number of cases to be selected (the k value). 
As jCOLIBRI 2.1 includes several new selection algorithms, the k
parameter has been removed from the KNNConfig object. This way,
the Nearest Neighbour method always returns all cases (as RetrievalResult objects)
and then, a selection algorithm must be executed.

Name changes:
jcolibri.retrieval.KNNretrieval --> jcolibri.retrieval.NNretrieval
KNNConfig --> NNConfig
KNNRetrievalMethod --> NNScoringMethod

== What's new in Version 2.0 ==

-- Text retrieval based on TFDF
Implemented through Apache Lucene.

-- Text clustering 
Implemente through Carrot2.

-- Classification and maintenance methods 
Developed by Derek Bridge and Lisa Cummins from University College Cork, 
Ireland.

-- Case base visualization methods 
Developed by Josep LLuís Arcos from Artificial Intelligence Research Institute,
Spanish Scientific Research Council.

-- Improved methods for textual CBR
Using OpenNLP and GATE new and more sophisticated similarity measures
have been included for textual CBR (like the compression based similarities
developed by Derek Bridge).

-- Improved connectors to store case bases
Connector to in databases through Hibernate. 
Connector to ontologies through OntoBridge, a simple wrapper for Jena/DIG
reasoner.
Connector to textual files.
For the connector to work cases have to be implemented as JavaBeans.

-- Improved ontology-based similarity measures
New ontology-based similarity functions are included. Ontologies are
accessed through OntoBridge what makes the implementation more robust
and efficient. 

-- Improved access to Wordnet
Wordnet is now easier to use than in version 1.1, and it can be loaded
into memory through our WordNetBridge library, so there is no need 
to install it separately.

-- Improved Tomcat integration
jCOLIBRI 2.0 jar file can be registered in a servlet engine and call
from servlets. Running jCOLIBRI 1.1 as a servlet required some bizarre
hacking.

-- Examples
jCOLIBRI includes 16 completely documented examples and a stand-alone
CBR application that uses the library to easily learn to use the framework.

== Third-party software packages  ==

jCOLIBRI includes as jar files the following third-party software packages:

Carrot2 (http://www.carrot2.org): MIT license
Chart2D (http://chart2d.sourceforge.net/): LGPL
Gate (http://gate.ac.uk/): LGPL
Hibernate (http://www.hibernate.org): LGPL
Jena (http://jena.sourceforge.net): HP custom license
Lucene (http://lucene.apache.org): Apache License version 2
OpenNLP (http://opennlp.sourceforge.net): LGPL
Pellet (http://pellet.owldl.com/): MIT license
Wordnet (http://wordnet.princeton.edu/): See license at 
                                         http://wordnet.princeton.edu/license


== License ==

The source code is released under the terms of the GNU Lesser 
General Public License (LGPL).

