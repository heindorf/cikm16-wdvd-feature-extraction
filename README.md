Synopsis
========
The Wikidata Vandalism Detector 2016 (WDVD-2016) is a machine learning-based approach for automatic vandalism detection in Wikidata. It was developed as a joint project between the University of Paderborn and the Bauhaus-Universität Weimar.

IMPORTANT NOTE: This is a preliminary release of our source code, primarily meant for participants of the [WSDM Cup 2017](http://www.wsdm-cup-2017.org/vandalism-detection.html). We will release the final version of the source code in the comming weeks (including geolocation data for IP addresses, improved README files, ...)

Paper
-----
This source code forms the basis for the Wikidata Vandalism Detector 2016 which was published at the CIKM 2016 conference. When using the code, please make sure to refer to it as follows:

Stefan Heindorf, Martin Potthast, Benno Stein, and Gregor Engels. Vandalism Detection in Wikidata. In Proceedings of the 25th ACM International Conference on Information and Knowledge Management (CIKM 16) (to appear), October 2016. ACM. <http://dx.doi.org/10.1145/2983323.2983740>

wdvd16-feature-extraction
-------------------------
Code extracting features from the Wikidata Vandalism Corpus 2015 (WDVC-2015).

The code was tested with Java 8 Update 77, 64 Bit under Windows 10.

### Installation
In Eclipse, executing "Run As -> Maven install" creates a JAR file which includes all dependencies.

### Execution
Usage:

	usage: java -jar wdvd-feature-extraction.jar
          [--labels <FILE>] [--revisiontags <FILE>] [--geodb <FILE>]
          REVISIONS FEATURES

Given a REVISIONS file (in bz2 format), extracts features and stores them in the FEATURES file (in bz2 format). If no files for labels, revision tags or the geolocation database are specified, the corresponding features will be 'NA'. The feature extraction assumes that the revisions are grouped by item in the same way as in the Wikidata Vandalism Corpus 2015.

Required Data
------------
- [Wikidata Vandalism Corpus 2015](http://www.uni-weimar.de/en/media/chairs/webis/corpora/corpus-wdvc-15/)
- [Tags](http://groups.uni-paderborn.de/wdqa/tags_201501181252.csv.bz2)
- Geolocation Data (will be provided later)


Contact
=======
For questions and feedback please contact:

Stefan Heindorf, University of Paderborn  
Martin Potthast, Bauhaus-Universität Weimar  
Benno Stein, Bauhaus-Universität Weimar  
Gregor Engels, University of Paderborn
