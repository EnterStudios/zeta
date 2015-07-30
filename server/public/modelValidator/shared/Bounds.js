// Generated by CoffeeScript 1.9.1
(function() {
  window.Bounds = (function() {
    Bounds.TOO_LOW = -1;

    Bounds.IN_BOUNDS = 0;

    Bounds.TOO_HIGH = 1;

    function Bounds(lowerBound, upperBound) {
      this.lowerBound = lowerBound;
      this.upperBound = upperBound;
    }

    Bounds.prototype.getLowerBound = function() {
      return this.lowerBound;
    };

    Bounds.prototype.getUpperBound = function() {
      return this.upperBound;
    };

    Bounds.prototype.compareTo = function(count) {
      var check;
      check = window.Bounds.IN_BOUNDS;
      if (count < this.lowerBound) {
        check = window.Bounds.TOO_LOW;
      } else if (this.upperBound !== -1 && count > this.upperBound) {
        check = window.Bounds.TOO_HIGH;
      }
      return check;
    };

    return Bounds;

  })();

}).call(this);

//# sourceMappingURL=Bounds.js.map