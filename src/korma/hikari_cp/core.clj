(ns korma.hikari-cp.core
  (:import [com.zaxxer.hikari HikariDataSource]))

(defn- as-properties [m]
  (let [p (java.util.Properties.)]
    (doseq [[k v] m]
      (.setProperty p (name k) (str v)))
    p))

(defn make-pool 
  [{:keys [classname ds-classname
           subprotocol subname jdbc-url 
           user password
           auto-commit connection-timeout
           max-lifetime minimum-idle
           idle-timeout maximum-pool-size
           test-connection-query]
    :or {idle-timeout (* 10 60 1000) 
         maximum-pool-size 15
         auto-commit true
         connection-timeout (* 30 1000)
         max-lifetime (* 30 60 1000)
         minimum-idle maximum-pool-size
         test-connection-query nil}
    :as spec}]
  (merge (select-keys spec [:naming :delimiters :alias-delimiter :subprotocol])
         {:datasource
          (doto (new HikariDataSource)
            (.setDataSourceClassName ds-classname)
            (cond-> classname (.setDriverClassName classname))
            (cond-> (or jdbc-url (and subprotocol subname))
              (.setJdbcUrl (or jdbc-url (str "jdbc:" subprotocol ":" subname))))
            (.setUsername user)
            (.setPassword password)
            (.setDataSourceProperties (as-properties
                                        (dissoc spec
                                                :make-pool? :classname :ds-classname
                                                :subprotocol :subname :jdbc-url
                                                :naming :delimiters :alias-delimiter
                                                :auto-commit :connection-timeout
                                                :max-lifetime :minimum-idle
                                                :idle-timeout :maximum-pool-size
                                                :user :password :test-connection-query)))
            (.setIdleTimeout idle-timeout)
            (.setMaximumPoolSize maximum-pool-size) 
            (.setAutoCommit auto-commit)
            (.setConnectionTimeout connection-timeout)
            (.setMaxLifetime max-lifetime)
            (.setMinimumIdle minimum-idle)
            (.setConnectionTestQuery test-connection-query))
          :make-pool? false}))
