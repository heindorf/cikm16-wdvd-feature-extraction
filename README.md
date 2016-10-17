Synopsis
========
The Wikidata Vandalism Detector 2016 (WDVD-2016) is a machine learning-based approach for automatic vandalism detection in Wikidata. It was developed as a joint project between Paderborn University and Bauhaus-Universität Weimar.

Paper
-----
This source code forms the basis for the Wikidata Vandalism Detector 2016 which was published at the CIKM 2016 conference. When using the code, please make sure to refer to it as follows:

Stefan Heindorf, Martin Potthast, Benno Stein, and Gregor Engels. Vandalism Detection in Wikidata. In Proceedings of the 25th ACM International Conference on Information and Knowledge Management (CIKM 16) (to appear), October 2016. ACM. <http://dx.doi.org/10.1145/2983323.2983740>

Feature Extraction Component
----------------------------
The feature extraction component performs the feature extraction for the Wikidata Vandalism Detector 2016 (WDVD-2016). The classification and evaluation can be done with the corresponding [classification component](https://github.com/heindorf/cikm16-wdvd-classification).

The code was tested with Java 8 Update 77, 64 Bit under Windows 10.

### Installation
In Eclipse, execute "Run As -> Maven install" to create a JAR file which includes all dependencies.

### Execution
Usage:

	java -jar wdvd-feature-extraction.jar
        [--labels <FILE>] [--revisiontags <FILE>] [--geodb <FILE>] [--geofeatures <FILE>]
        REVISIONS FEATURES

Given a REVISIONS file (in bz2 format) and labels file (in bz2 format), extracts features and stores them in the FEATURES file (in bz2 format). If files for revisiontags and geolocation are specified, the corresponding features will be computed; otherwise, they will be 'NA'. The feature extraction assumes that the revisions are grouped by item in the same way as in the Wikidata Vandalism Corpus 2015.

Note: The options `geodb` and `geofeatures` are mutually exclusive. The first option expects a geolocation database which we cannot provide to you due to copyright issues. As an alternative, you can use the second option which uses precomputed geolocation features.

Example:

	java -jar wdvd-feature-extraction.jar
		--labels wdvc15-ground-truth.csv.bz2
		--revisiontags wdvc15-tags.csv.bz2
        --geofeatures wdvd16-features.csv.bz2
		wdvc15-revisions.xml.bz2
		wdvd16-features2.csv.bz2

	

Required Data
------------
- [Wikidata Vandalism Corpus 2015](http://www.uni-weimar.de/en/media/chairs/webis/corpora/corpus-wdvc-15/)
- [Tags](http://groups.uni-paderborn.de/wdqa/cikm16/wdvc15-tags.csv.bz2) (optional)
- [Precomputed Geolocation Features](http://groups.uni-paderborn.de/wdqa/cikm16/wdvd16-features.csv.bz2) (optional)


Contact
=======
For questions and feedback please contact:

Stefan Heindorf, Paderborn University  
Martin Potthast, Bauhaus-Universität Weimar  
Benno Stein, Bauhaus-Universität Weimar  
Gregor Engels, Paderborn University

License
=======

Wikidata Vandalism Detector 2016 by Stefan Heindorf, Martin Potthast, Benno Stein, Gregor Engels is licensed under a MIT license.
