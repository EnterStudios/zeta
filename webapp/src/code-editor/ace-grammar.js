import AceGrammar from 'string-replace-loader?search=_ace.require&replace=_ace.acequire!string-replace-loader?search=require\.specified&replace=false&flags=g!ace-grammar/build/ace_grammar'
global.AceGrammar = AceGrammar;