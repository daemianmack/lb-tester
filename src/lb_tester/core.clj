(ns lb-tester.core
  (:require [clj-http.client :as client]
            [clj-http.cookies :refer [cookie-store]]
            [clojure.string :refer [trim]]))

(defn -main
  []
  (let [cs (cookie-store)]
    (loop [n 5000]
      (let [res (client/get "https://zorg-env.us-east-1.elasticbeanstalk.com/build"
                            {:cookie-store cs
                             :insecure? true})
            now (.getTime (java.util.Date.))]
        (println now
                 \tab
                 (:status res)
                 \tab
                 (:request-time res)
                 \tab
                 (-> res :headers (get "hostname"))
                 \tab
                 (-> res :body trim)
                 \tab
                 (format "cookie=%s"
                         (-> res :cookies (get "AWSELB") :value)))
        (when (pos? n)
          (recur (dec n)))))))
