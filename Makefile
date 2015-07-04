JFLAGS = -g:none 
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  Enigma.java \
		  enigma/machine/EnigmaMachine.java \
		  enigma/machine/EnigmaMachineStepper.java \
		  enigma/machine/Plugboard.java \
		  enigma/machine/Reflector.java \
		  enigma/machine/Rotor.java \
		  \
		  enigma/commandline/settings/PlugboardSettings.java \
		  enigma/commandline/settings/ReflectorSettings.java \
		  enigma/commandline/settings/RotorSettings.java \
		  enigma/commandline/settings/Settings.java \
		  enigma/commandline/settings/SettingsParser.java \
		  enigma/commandline/settings/SettingsParserException.java \
		  enigma/commandline/ui/UI.java \
		  enigma/commandline/ui/UIPrinter.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class enigma/machine/*.class enigma/commandline/settings/*.class enigma/commandline/ui/*.class
