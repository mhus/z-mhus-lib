This project includes a fork from https://github.com/MorphiaOrg/morphia. It's under Apache 2.0 Licence and compatible with the
licence of this project.

Reason of the fork is the enclosed dependencies and the morphia project and that the project is unable to let other annotations overwrite
the actual functionality.

The fork is accessible via https://github.com/mhus/morphia and parts will be synchronized with this project.

fork -> mhu-lib-mongo

cp -r morphia/morphia/src/main/java/dev mhus-lib/mhu-lib-mongo/src/main/java/
cp -r morphia/morphia/src/main/java/relocated mhus-lib/mhu-lib-mongo/src/main/java/

mhu-lib-mongo -> fork

cp -r mhus-lib/mhu-lib-mongo/src/main/java/dev morphia/morphia/src/main/java/
