package dictionary

class DictionaryProvider {
    companion object{
        fun createDictionary(type: DictionaryTypes) : IDictionary{
            return when(type){
                DictionaryTypes.ARRAY_LIST -> ListDictionary
                DictionaryTypes.HASH_SET -> HashDictionary
                DictionaryTypes.TREE_SET -> TreeDictionary
            }
        }
    }
}