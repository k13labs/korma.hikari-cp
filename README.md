# korma.hikari-cp [![Travis CI Build](https://travis-ci.org/k13labs/korma.hikari-cp.svg?branch=master)](https://travis-ci.org/k13labs/korma.hikari-cp)

Experimental HikariCP support for SQL Korma.

[![Clojars Project](http://clojars.org/korma.hikari-cp/latest-version.svg)](http://clojars.org/korma.hikari-cp)

## Getting started

Simply add korma.hikari-cp as a dependency to your lein project:

```clojure
[korma.hikari-cp "0.1.0"]
```

## Usage

```clojure
(use 'korma.db 'korma.hikari-cp.core)

(defdb mydb1
  (make-pool (h2 {:db "mem:sample1"})))
  
(defdb mydb2
  (make-pool {:jdbc-url "jdbc:mysql://localhost:3306/sample2"}))

(defdb mydb3
  (make-pool {:ds-classname "org.postgresql.ds.PGSimpleDataSource"
              :databaseName "sample3"
              :serverName "localhost"}))
```

## License

Copyright © 2015 Jose Gomez

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
