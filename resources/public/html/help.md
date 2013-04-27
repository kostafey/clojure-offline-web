<!-- # This is a test -->
<!-- some code follows -->
<!-- ```clojure -->
<!-- (defn foo []) -->
<!-- ``` -->

# Long story #

## 1. Install leiningen

You can download `lein` script as usal from `leiningen.org` and place it
somewhere in the `PATH` environment variable.

The actual location of the `leiningen-%LEIN_VERSION%-standalone.jar` is holds
in the `LEIN_JAR_URL` variable in the lein script (or can be seen in the
error log when you run lein self-install). Download and place this jar in the
%HOME%\.lein\self-installs\

To ensure lein is installed correctly you can run `lein' from the shell.

## 2. Install lein-localrepo (leiningen plugin)

Download `lein-localrepo-<version>.jar` file. The probable location of the
file can be obtained by evaluating the following elisp script:
```
(clojure-offline-guess-clojars-url [lein-localrepo "0.4.1"])
```
Place it to the %HOME%\.m2\repository\<group-id>\<artifact-id>\<version>\
folder, e.g. %HOME%\.m2\repository\lein-localrepo\lein-localrepo\0.4.1\
Create the `lein-localrepo-0.4.1.pom' file in the same folder (this file 
can be empty).

Install as a global plugin in ~/.lein/profiles.clj:

```
{:user {:plugins [[lein-localrepo "0.4.1"]]}}
```

To ensure lein-localrepo is installed correctly you can run the shell:

lein localrepo help

## 3. Download and install all required libraries

Select (mark) :dependencies vector in the defproject expression of the
`project.clj' file, like here (selection is marked by # symbols):

```
:dependencies #[[org.clojure/clojure "1.5.1"]
                [compojure "1.1.5"]
                [me.raynes/laser "1.1.1"]
                [mysql/mysql-connector-java "5.1.24"]
                [korma "0.3.0-RC5"]
                [lib-noir "0.4.9"]]#
```

Run M-x clojure-offline-create-script RET RET

WARNING!

There is no guarantee that the file is located in the printed path.
E.g. https://clojars.org/repo/org/clojure/clojure/1.5.1/clojure-1.5.1.jaris
a wrong path since clojure-1.5.1.jar is not hosted in the clojars.org
(yet). So, you should find it manually. But if the jar is hosted in the
clojars.org, the printed url is likely correct.

Repeat this for :plugins and :dev :dependencies sections.

## 4. Create *.pom files

Create *.pom files in all apropriate .m2 subfolders (like in the point 2).

Try to run M-x nrepl-jack-in RET

## 5. Correct errors

The problems and ideas to solve them during installations of the 
[lein-ring "0.8.3"] plugin (as an example) are described below:

In the simple cases when you run, e.g.:
* M-x `nrepl-jack-in' 
* lein help
* lein uberjar
* lein ring uberwar
* lein ring server
the error occurrence shows us what additional jars is required, e.g.:

```
Could not transfer artifact org.clojure:clojure:pom:1.5.1 from/to central 
(http://repo1.maven.org/maven2/): repo1.maven.org
This could be due to a typo in :dependencies or network issues.
```

So, it should be installed:
(clojure-offline-create-script ["org.clojure:clojure:pom:1.5.1"])

Or several artifacts is needed:

```
error in process sentinel:
Could not start nREPL server:
Could not transfer artifact org.clojure:tools.nrepl:pom:0.2.1 from/to central
(http://repo1.maven.org/maven2/): repo1.maven.org
Could not transfer artifact clojure-complete:clojure-complete:pom:0.2.2
from/to central (http://repo1.maven.org/maven2/): repo1.maven.org
This could be due to a typo in :dependencies or network issues.
```

Again:
(clojure-offline-create-script ["org.clojure:tools.nrepl:pom:0.2.1"
                        "clojure-complete:clojure-complete:pom:0.2.2"])

Don't forget to create *.pom files every time!

The hard problem looks like this:

```
Problem loading: Could not locate leinjacker/deps__init.class or 
leinjacker/deps.clj on classpath:
```

So, install `leinjacker' with all dependences and try again. The problem
remains...  It is not mean that `leinjacker' is not installed. It is not mean
that leinjacker's dependences is not installed. It is not mean that
dependences of the leinjacker's dependences is not installed... and so on...
ok :)

In my case the problem was in the `lein-ring' (with empty *.pom file in the
.m2 directory) dependences, wich is requires `leinjacker' (wich is already
installed) and `org.clojure/data.xml' (wich is not installed yet in this
example).

To see the full list of the dependences you can use 
`https://clojureoffline-kostafey.rhcloud.com.

BTW, if the `lein-ring's *.pom file were available and correct (not empty) in
the maven cashed directory (.m2) you will see the following error insted of
the previous:

| Could not transfer artifact org.clojure:data.xml:pom:0.0.6 from/to central
| (http://repo1.maven.org/maven2/): repo1.maven.org
| This could be due to a typo in :dependencies or network issues.

In the case where all jar dependences is resolved the following problem
occurs:

| Could not locate clojure/core/contracts/impl/transformers__init.class or 
| clojure/core/contracts/impl/transformers.clj on classpath:

The only way to resolve - provede all necessary pom file. Re-run and, the
actual problem shoud be shown:

| Could not transfer artifact org.clojure:pom.contrib:pom:0.0.26 from/to 
| central (http://repo1.maven.org/maven2/): repo1.maven.org

Copy from
https://maven-us.nuxeo.org/nexus/content/groups/public/org/clojure/pom.contrib/0.0.26/pom.contrib-0.0.26.pom
to
~/.m2/repository/org/clojure/pom.contrib/0.0.26/pom.contrib-0.0.26.pom

| Could not transfer artifact org.sonatype.oss:oss-parent:pom:5 from/to 
| central (http://repo1.maven.org/maven2/): repo1.maven.org

http://repo1.maven.org/maven2/org/sonatype/oss/oss-parent/5/oss-parent-5.pom
~/.m2/repository/org/sonatype/oss/oss-parent/5/oss-parent-5.pom


