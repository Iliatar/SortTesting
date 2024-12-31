prepareDist: install moveDist
fullCycle: install moveDist runDist

install:
	./gradlew installDist
moveDist:
	cp -R /home/iliatar/java-practice/SortTesting/app/build/install/app /home/iliatar/java-practice/SorterDistributive
runDist:
	/home/iliatar/java-practice/SorterDistributive/app/bin/app /home/iliatar/java-practice/SortTesting/TestConfig.json
