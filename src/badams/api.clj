(ns badams.api
  (:require [badams.core :as core]
            [badams.repository :as repo]
            [bidi.ring :refer [make-handler]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.util.response :as res]))

(defn add-to-repo-handler [{:keys [components/repo body] :as request}]
  (if-let [ok (some->> (core/parse (slurp body))
                       (repo/put! repo))]
    (res/created "/records")
    (res/bad-request {:error "unparseable"})))

(def ^:private not-found
  (constantly (res/not-found {:error "Not Found"})))

(defn- sorting-handler [order]
  (fn [{:keys [components/repo] :as request}]
    (->> (repo/values repo {:order order})
         (map core/prepare)
         res/response)))

(def ^:private handler
  "Routing to records handlers
  GET records/ is left unsorted to serve at a Location for POST records/"
  (make-handler
   ["/" [["records/"
          [[:post [["" add-to-repo-handler]]]
           [:get  [["" (sorting-handler repo/unsorted)]]]
           [:get  [["email"     (sorting-handler repo/by-email-desc-last-name-asc)]
                   ["birthdate" (sorting-handler repo/by-birth-date-asc)]
                   ["name"      (sorting-handler repo/by-last-name-desc)]]]]]
         [true not-found]]]))

(defn- with-components [component-map handler]
  (fn [request]
    (handler (merge request component-map))))

(defn app [components]
  (->> #'handler
       (with-components components)
       wrap-json-response
       wrap-reload))
