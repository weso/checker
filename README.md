# Checker

## Author & contributors

* Author: [Jose Emilio Labra Gayo](http://labra.weso.es)

## Contribution

Contributions are greatly appreciated.
Please fork this repository and open a
pull request to add more features or [submit issues](https://github.com/weso/checker/issues)

## Publishing to OSS-Sonatype

This project uses [the sbt ci release](https://github.com/olafurpg/sbt-ci-release) plugin for publishing to [OSS Sonatype](https://oss.sonatype.org/).

##### SNAPSHOT Releases
Open a PR and merge it to watch the CI release a -SNAPSHOT version

##### Full Library Releases
1. Increment the version number in `version.sbt` to the desired release version number.
2. Commit and push this change with a message like "blah blah version 0.x.x".
3. Push a tag and watch the CI do a regular release
4. `git tag -a v0.1.0 -m "v0.1.0"`
5. `git push origin v0.1.0`
_Note that the tag version MUST start with v._
