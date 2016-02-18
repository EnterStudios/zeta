var mEnum = (function () {
    'use strict';

    var _mEnumContainer;

    var init;
    var getMEnumContainer;
    var getMEnumNames;
    var getMEnums;
    var getMEnum;

    init = function init (mEnumContainer) {
        _mEnumContainer = mEnumContainer;
    };

    getMEnumContainer = function getMEnumContainer () {
        if (!_mEnumContainer) {
            _mEnumContainer = new joint.dia.Element({
                size : {
                    width : 0,
                    height : 0
                },
                position : {
                    x : 0,
                    y : 0
                },
                id : 'menum_container',
                type : 'mcore.Enum',
                markup : '<g />',
                name : 'mEnumContainer',
                'm_enum' : []
            });
        }
        return _mEnumContainer;
    };

    getMEnumNames = function getMEnumNames () {
        var types = [];
        if (_mEnumContainer.attributes['m_enum']) {
            _.each(_mEnumContainer.attributes['m_enum'], function (mEnum) {
                types.push(mEnum.name);
            });
        }
        return types;
    };

    getMEnums = function getMEnums () {
        return _mEnumContainer.attributes['m_enum'];
    };

    getMEnum = function getMEnum (mEnumName) {
        var retEnum = {};

        _.each(getMEnums(), function (mEnum) {
            if (mEnum.name === mEnumName) {
                retEnum = mEnum;

                // stop iteration
                return false;
            }
        });

        return retEnum;
    };

    return {
        init : init,
        getMEnumContainer : getMEnumContainer,
        getMEnumNames : getMEnumNames,
        getMEnums : getMEnums,
        getMEnum : getMEnum
    };

})();