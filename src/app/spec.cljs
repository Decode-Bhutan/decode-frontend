(ns app.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [clojure.set :as set]
            [clojure.test.check.generators :as gen]))

;; api


;; data
(def bcse
   ["AREKHA MSS","AUTSHO CS","BABESA HSS","BAJOTHANG HSS","BARTSHAM CS","BAYLING CS","BITEKHA MSS","BJISHONG CS","BULI CS","CHANGANGKHA MSS","CHANGZAMTOG MSS","CHAPCHA MSS","CHASKHAR CS","CHUKHA CS","CHUMEY MSS","CHUMIGTHANG MSS","CHUNDU ARMED FORCES PS","DAGA CS","DAGAPELA MSS","DAMPHU CS","DARLA MSS","DASHIDING HSS","DECHENTSEMO CS","DECHHENCHOELING HSS","DEKILING MSS","DOROKHA CS","DR.TOBGYEL PRIMARY SCHOOL","DRAMETSE CS","DRUK PRIMARY SCHOOL","DRUKGYEL CS","DRUKJEGANG CS","DUNGTSE CS","ELC HIGH SCHOOL","GARPAWONG MSS","GASELO CS","GEDU HSS","GELEPHU HSS","GELEPHU MSS","GESARLING CS","GOMDAR CS","GOMTU HSS","GONGTHUNG MSS","GONGZIM UGYEN DORJI CS","GYELPOZHING CS","JAKAR HSS","JAMPEL HSS","JAMPELING CS","JIGME SHERUBLING CS","JOMOTSHANGKHA MSS","KABESA CS","KAMJI  CS","KARMALING HSS","KENGKHAR MSS","KHANGKHU MSS","KHASADRAPCHU MSS","KHURUTHANG MSS","KIDHEYKHAR MSS","KUNZANGLING CS","KUZUCHEN MSS","LAMGONG MSS","LAYA CS","LHAMOYZINGKHA CS","LHUENTSE HSS","LOSELING MSS","LUNGTENPHU MSS","LUNGTENZAMPA MSS","MARTSHALLA CS","MENDREGANG CS","MINJEY MSS","MINJIWOONG CS","MONGAR HSS","MONGAR MSS","MOTITHANG HSS","NAGOR MSS","NANGKOR CS","NGANGLAM CS","NORBUGANG CS","NORBULING CS","ORONG CS","PAKSHIKHA CS","PELJORLING HSS","PELKHIL SCHOOL","PELRITHANG MSS","PEMAGATSHEL MSS","PHOBJIKHA CS","PHUENTSHOLING HSS","PHUENTSHOLING MSS","PHUNTSHOTHANG MSS","PUNAKHA CS","RADHI MSS","RAMJAR MSS","RANGJUNG CS","SAMCHOLING MSS","SAMDRUP JONGKHAR MSS","SAMTENGANG CS","SAMTSE HSS","SARPANG CS","SARPANG MSS","SHABA HSS","SHARI HSS","SHERUBLING CS","SONAMGANG MSS","SONAMTHANG CS","TAKTSE CS","TANG CS","TANGMACHU CS","TASHIDINGKHA MSS","TENDRUK CS","THE ROYAL ACADEMY","THRIMSHING CS","TRASHIGANG MSS","TRASHITSE HSS","TSANGKHA CS","TSENKHARLA CS","TSHANGKHA CS","TSIRANGTOE CS","UDZORONG CS","UGYEN ACADEMY","URA CS","UTPAL ACADEMY","WANAKHA CS","WANGBAMA CS","WANGCHU MSS","YADI CS","YANGCHENGATSHEL MSS","YANGCHENPHUG HSS","YEBILAPTSA MSS","YELCHEN CS","YOESELTSE MSS","YURUNG CS","ZHEMGANG CS","ZHILUKHA MSS"])
(comment
  (count bcse))
(def bhsec
  ["BABESA HSS","BAJOTHANG HSS","BARTSHAM CS","BAYLING CS","BJISHONG CS","CHUKHA CS","DAGA CS","DAMPHU CS","DASHIDING HSS","DECHHENCHOELING HSS","DESI HIGH SCHOOL","DOROKHA CS","DRAMETSE CS","DRUK PRIMARY SCHOOL","DRUKGYEL CS","DRUKJEGANG CS","DUNGSAM ACADEMY","ELC HIGH SCHOOL","GASELO CS","GEDU HSS","GELEPHU HSS","GESARLING CS","GOMTU HSS","GONGZIM UGYEN DORJI CS","GYELPOZHING CS","JAKAR HSS","JAMPEL HSS","JAMPELING CS","JIGME SHERUBLING CS","KARMA ACADEMY ","KARMALING HSS","KELKI HSS","KUENDRUP HSS","KUENGA HSS","LHUENTSE HSS","LOSEL GYATSHO ACADEMY","MENDREGANG CS","MONGAR HSS","MOTITHANG HSS","NANGKOR CS","NGANGLAM CS","NIMA HSS","NORBU ACADEMY ","NORBULING CS","ORONG CS","PAKSHIKHA CS","PELJORLING HSS","PELKHIL SCHOOL","PHUENTSHOLING HSS","PUNAKHA CS","RANGJUNG CS","RigZom ACADEMY","RINCHEN HSS","SAMTSE HSS","SARPANG CS","SHABA HSS","SHARI HSS","SHERUB RELDRI HSS","SHERUBLING CS","SONAM KUENPHEN HSS","SONAMTHANG CS","TANGMACHU CS","TENDRUK CS","TRASHITSE HSS","TSENKHARLA CS","TSHANGKHA CS","UGYEN ACADEMY","UTPAL ACADEMY","WANGBAMA CS","YADI CS","YANGCHENPHUG HSS","YOEZERLING HSS","YONTEN KUENJUNG ACADEMY","ZHEMGANG CS"])
(comment
  (count bhsec))
(comment
  (apply + (list (count bhsec) (count bcse))))

(comment
  (count (into #{} (merge bcse bhsec))))
(comment
  (apply + (list (* 2 (count (set/intersection (into #{} bhsec) (into #{} bcse))))
                 (count (set/difference (into #{} bhsec) (into #{} bcse)))
                 (count (set/difference (into #{} bcse) (into #{} bhsec))))))

(defprotocol IFilter
  (filter-by [s]))

(extend-protocol
  IFilter
  cljs.core/PersistentVector
  (filter-by [vec]
    ;; https://clojuredocs.org/clojure.core/reduce
    (reduce
     (fn [acc curr]
      (assoc acc (keyword curr) {:key curr
                                 :results (filter-by curr)}))
     {}
     vec))
  string
  (filter-by [st]
    (filter (fn [str]
              (str/includes? str
               (str/upper-case st)))
      (seq (merge bcse bhsec)))))


(defn cs? [school]
  (str/includes? school
   (str/upper-case "cs")))
(defn hss? [school]
  (or
    (str/includes? school
     (str/upper-case "hss"))
    (str/includes? school
     (str/upper-case "high"))))
(defn mss? [school]
  (str/includes? school
   (str/upper-case "mss")))
(defn ps? [school]
  (str/includes? school
   (str/upper-case "ps")))

(defn academy? [school]
  (str/includes? school
   (str/upper-case "academy")))
(defn primary? [school]
  (str/includes? school
   (str/upper-case "primary")))

(defn calc-school-type [school]
  (cond
    (cs? school) :central
    (hss? school) :high
    (mss? school) :middle
    (ps? school) :public
    (academy? school) :academy
    (primary? school) :academy
    :else :unknown))

(defn process-school [school]
  {:school/name school
   :school/type (calc-school-type school)})

(comment)


(defn process-schools [schools]
  (map process-school schools))

(comment
  (filter #(= :unknown (:school/type %)) (process-schools (distinct (concat bcse bhsec))))
  (filter #(= :central (:school/type %)) (process-schools (distinct (concat bcse bhsec)))))

(comment
  (filter-by "Yang")
  (filter-by "Dr")
  (apply #{} (filter-by " cs")
             (filter-by " mss")
             (filter-by " hss"))
  (filter-by ["Yang" "Lung" "Moti" "Sun"])
  (filter-by ["Yang" "Lung" "Moti" "Dr"]))
(comment
  (filter (fn [str]
            (string/includes? str
             (string/upper-case "Yang")))
    (seq (merge bcse bhsec)))
  (filter (fn [str]
            (string/includes? str
             (string/upper-case "Yang")))
    (seq (merge bcse bhsec))))

(comment
  ;; 4:33 PM
  (string/includes?
    (string/upper-case "Hello")
    (string/upper-case "He")))

(s/def :school/id uuid?)
(s/def :school/name
  (s/with-gen
    string?
    #(s/gen #{"Yonphula Lower Secondary School"
              "Sunshine School"
              "Lungtenphu Lower Secondary School"
              "Lungtenzampa Middle Secondary School"
              "YHSS"})))

(comment
 (gen/generate (s/gen :school/name))
 (gen/generate (s/gen :school/id)))
(s/def :school/type #{:mss :cs :hss})
(s/def :school/level string?)
(s/def :school/phone string?)

(s/def ::school (s/keys :req [:school/id :school/name :school/type :school/level]
                        :opt [:school/phone]))

(comment
   (gen/generate (s/gen ::school)))


(s/def :student/name string?)
(s/def :student/school ::school)
