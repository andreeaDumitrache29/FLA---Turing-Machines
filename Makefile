JFLAGS = -g
JC = javac
JVM = java
JFLEX = ./lib/jflex-1.6.1.jar

all: build

Flexer.java: flexer.flex
	jflex $<

sources = $(wildcard *.java)
classes = $(sources:.java=.class)

build: Flexer.java $(classes)

run: build
	$(JVM) Main '$(value arg1)' '$(value arg2)' '$(value arg3)'

.PHOONY: clean
clean :
	rm -f *.class Flexer.java *~
%.class : %.java
	$(JC) $(JFLAGS) $<
pack: clean
	zip archive_`date +'%s'`.zip README Makefile *.c *.h *.cpp *.hpp *.cxx *.hxx *.cc *.hh *.c++ *.h++ *.C *.H *.java *.l *.lex *.flex
