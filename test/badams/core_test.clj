(ns badams.core-test
  (:require [badams.core :as sut]
            [clojure.test :refer :all]))

(def date "12/12/1979")

(def details {:LastName      "LN"
              :FirstName     "FN"
              :Email         "EM"
              :FavoriteColor "FC"
              :DateOfBirth   (sut/date date)})

(def csv-line      "LN,FN,EM,FC,12/12/1979")
(def space-sv-line "LN FN EM FC 12/12/1979")
(def pipe-sv-line  "LN|FN|EM|FC|12/12/1979")

(def expected-details {:LastName      "LN"
                       :FirstName     "FN"
                       :Email         "EM"
                       :FavoriteColor "FC"
                       :DateOfBirth   date})

(deftest parse-test
  (and
   (is (= nil (sut/parse nil)))
   (is (= nil (sut/parse "")))

   (doseq [line [csv-line space-sv-line pipe-sv-line]]
     (is (= details (sut/parse line))))))

(deftest prepare-details-test
  (is (= expected-details (sut/prepare details))))
