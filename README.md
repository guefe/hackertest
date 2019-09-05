# Getting Started

### Project structure
The project contains two main packages:

* hackertest: implementation of the first part of the assessment, related to the hacker detection system.
  * detector (and .impl): implementation of the suggested interface HackerDetector.
  * lineparser: log line parser.
  * model: containing VOs
* timetest: implementation of the second part of the assessment, related to time calculation.

### Implementation summary
The class MyHackerDetectorImpl implements the suggested interface. 
It will track only failed attempts, using a Map for quickly accessing relevant data for every IP. It will clean that map up with every parseLine invocation, so that old log lines of any IP are no longer kept in memory.