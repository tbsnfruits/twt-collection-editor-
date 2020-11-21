(ns twt-collection-editor.api
  (:require [cheshire.core :refer :all]
            [clj-http.client :as client]
            [twt-collection-editor.auth :as auth]))

(def apis {:collections-entries        {:method :get
                                         :url    "https://api.twitter.com/1.1/collections/entries.json"}
            :collections-entries-add    {:method :post
                                         :url    "https://api.twitter.com/1.1/collections/entries/add.json"}
            :collections-entries-curate {:method :post
                                         :url    "https://api.twitter.com/1.1/collections/entries/curate.json"}
            :collections-entries-move   {:method :post
                                         :url    "https://api.twitter.com/1.1/collections/entries/move.json"}
            :collections-entries-remove {:method :post
                                         :url    "https://api.twitter.com/1.1/collections/entries/remove.json"}})

(defmacro make-endpoint-function
  [endpoint-name]
  (let [endpoint (endpoint-name apis)
             method (:method endpoint)
             url (:url endpoint)]
    `(defn ~(symbol endpoint-name)
       [& {:as params#}]
       (~(symbol (str "client/" (name method))) ~url
         {:query-params   (merge (auth/credentials ~method ~url params#) params#)
          :cookie-policy  :standard
          :decode-cookies false}) )))

(make-endpoint-function :collections-entries)
(make-endpoint-function :collections-entries-add)
;(make-endpoint-function :collections-entries-curate)        ;curate not working yet
(make-endpoint-function :collections-entries-move)
(make-endpoint-function :collections-entries-remove)

;;TODO - unfinished below
(defn collections-entries-curate
  "makes POST collections/entries/curate request"
  ;e.g.) (collections-entries-curate :id "custom-1287073494606389248"
  ;                                   :changes [{:op "add"
  ;                                              :tweet_id "390839888012382208"}
  ;                                             {:op "add"
  ;                                              :tweet_id          "390897780949925889",
  ;                                              :relative_to "1240723216412205062",
  ;                                              :above       false}])
  [& {:as body}]
  (let [url "https://api.twitter.com/1.1/collections/entries/curate.json"]
    (client/post url
                 {:query-params (auth/credentials :post url body)
                  :headers             {}
                  :body                (generate-string body)
                  :cookie-policy       :standard
                  :content-type        :application/json
                  :decode-cookies      false
                  :debug?              true
                  :debug-body true})))

(collections-entries-curate :id "custom-1287073494606389248"
                            :changes {:op       "add",
                                      :tweet_id "390839888012382208"})
