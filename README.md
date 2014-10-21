FormScanner
===

FormScanner is an OMR (Optical Mark Recognition) software that automatically marks multiple-choice papers. 
FormScanner not bind you to use a default template of the form, but gives you the ability to use a custom template created from a simple scan of a blank form.
The modules can be scanned as images with a simple scanner and processed with FormScanner software.
All the collected information can be easily exported to a spreadsheet.

---

Build requirements
===

* [Java 6](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven (2 or 3)](http://maven.apache.org/)
* [Mercurial](http://mercurial.selenic.com/) (I'm using [TortoiseHg](http://tortoisehg.bitbucket.org/) with Windows Explorer "shell" integration all-in-one installer with TortoiseHg 3.1.1 and Mercurial 3.1.1)

---

Compiling the software
===

$ hg clone http://hg.code.sf.net/p/formscanner/code formscanner-code
$ cd formscanner-code
$ mvn clean install

---

How To Contribute
===

[http://www.bugheaven.com/bugzilla/FormScanner](http://www.bugheaven.com/bugzilla/FormScanner)