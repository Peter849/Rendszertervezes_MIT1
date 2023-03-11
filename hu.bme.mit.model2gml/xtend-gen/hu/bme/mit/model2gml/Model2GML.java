package hu.bme.mit.model2gml;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class Model2GML {
  public String transform(final EObject root) {
    List<EObject> _list = IteratorExtensions.<EObject>toList(root.eAllContents());
    return this.transform(IterableExtensions.<EObject>toList(Iterables.<EObject>concat(Collections.<EObject>unmodifiableList(CollectionLiterals.<EObject>newArrayList(root)), _list)));
  }
  
  public String transform(final List<EObject> model) {
    String _xblockexpression = null;
    {
      final Map<EObject, Integer> objectToID = new HashMap<EObject, Integer>();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("graph");
      _builder.newLine();
      _builder.append("[");
      _builder.newLine();
      {
        boolean _hasElements = false;
        for(final EObject object : model) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate("\n", "\t");
          }
          _builder.append("\t");
          int _size = objectToID.size();
          int _plus = (_size + 1);
          CharSequence _transformObject = this.transformObject(object, _plus, objectToID);
          _builder.append(_transformObject, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        for(final EObject from : model) {
          {
            EList<EReference> _eAllReferences = from.eClass().getEAllReferences();
            for(final EReference reference : _eAllReferences) {
              {
                boolean _isMany = reference.isMany();
                if (_isMany) {
                  {
                    Object _eGet = from.eGet(reference);
                    for(final EObject target : ((List<EObject>) _eGet)) {
                      _builder.append("\t");
                      CharSequence _transformRelation = this.transformRelation(reference, from, target, objectToID);
                      _builder.append(_transformRelation, "\t");
                      _builder.newLineIfNotEmpty();
                    }
                  }
                } else {
                  _builder.append("\t");
                  Object _eGet_1 = from.eGet(reference);
                  CharSequence _transformRelation_1 = this.transformRelation(reference, from, ((EObject) _eGet_1), objectToID);
                  _builder.append(_transformRelation_1, "\t");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
        }
      }
      _builder.append("]");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  protected final int titleSize = 16;
  
  protected final int attributeSize = 14;
  
  protected final int borderDistance = 6;
  
  protected final int attributeBorderDistance = 8;
  
  protected final double ratio = (11.0 / 20.0);
  
  protected CharSequence transformObject(final EObject object, final int id, final Map<EObject, Integer> objectToID) {
    CharSequence _xblockexpression = null;
    {
      final CharSequence title = this.transormTitle(object, id);
      final Function1<EAttribute, CharSequence> _function = (EAttribute it) -> {
        return this.transformAttribute(it, object.eGet(it, true));
      };
      final List<CharSequence> attributes = ListExtensions.<EAttribute, CharSequence>map(object.eClass().getEAllAttributes(), _function);
      int _length = title.length();
      int _multiply = (_length * this.titleSize);
      double width = (_multiply + (this.borderDistance * 2));
      final Function1<CharSequence, Integer> _function_1 = (CharSequence it) -> {
        int _length_1 = it.length();
        int _multiply_1 = (_length_1 * this.attributeSize);
        int _plus = (_multiply_1 + (this.borderDistance * 2));
        return Integer.valueOf((_plus + (this.attributeBorderDistance * 2)));
      };
      List<Integer> _map = ListExtensions.<CharSequence, Integer>map(attributes, _function_1);
      for (final Integer x : _map) {
        width = Math.max(width, (x).intValue());
      }
      width = (width * this.ratio);
      int _size = attributes.size();
      int _plus = (_size + 1);
      int _multiply_1 = (_plus * this.attributeSize);
      int _plus_1 = (_multiply_1 + (this.borderDistance * 2));
      final int height = Math.max(
        (this.titleSize + 4), _plus_1);
      objectToID.put(object, Integer.valueOf(id));
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("node");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("[");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("id\t");
      _builder.append(id, "\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("graphics");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("[");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("w\t");
      _builder.append(width, "\t\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t");
      _builder.append("h\t");
      _builder.append(height, "\t\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t");
      _builder.append("type\t\"rectangle\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("fill\t\"#FFFFFF\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("fill2\t\"#FFFFFF\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("outline\t\"#000000\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("]");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("LabelGraphics");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("[");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("text\t\"");
      _builder.append(title, "\t\t\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t");
      _builder.append("outline\t\"#000000\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("fill\t\"#FFFFFF\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("fontSize\t");
      _builder.append(this.titleSize, "\t\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t");
      _builder.append("fontName\t\"Monospace\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("autoSizePolicy\t\"node_width\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("anchor\t\"t\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("borderDistance\t0.0");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("]");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("LabelGraphics");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("[");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("text\t\"");
      _builder.newLine();
      {
        for(final CharSequence attribute : attributes) {
          _builder.append(attribute);
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t");
      _builder.append("fontSize\t");
      _builder.append(this.attributeSize, "\t\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t");
      _builder.append("fontName\t\"Consolas\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("alignment\t\"left\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("anchor\t\"tl\"");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("borderDistance\t");
      _builder.append(this.borderDistance, "\t\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("]");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("]");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected CharSequence transormTitle(final EObject object, final int id) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("o");
    _builder.append(id);
    _builder.append(": ");
    String _name = object.eClass().getName();
    _builder.append(_name);
    return _builder;
  }
  
  protected CharSequence _transformAttribute(final EAttribute attribute, final Void value) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = attribute.getName();
    _builder.append(_name);
    _builder.append(" = null");
    return _builder;
  }
  
  protected CharSequence _transformAttribute(final EAttribute attribute, final Object value) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = attribute.getName();
    _builder.append(_name);
    _builder.append(" = ");
    CharSequence _transformAttributeValue = this.transformAttributeValue(value, value.getClass());
    _builder.append(_transformAttributeValue);
    return _builder;
  }
  
  protected CharSequence _transformAttribute(final EAttribute attribute, final List<?> values) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = attribute.getName();
    _builder.append(_name);
    _builder.append(" = [");
    final Function1<Object, CharSequence> _function = (Object it) -> {
      return this.transformAttributeValue(it, attribute.getEType().getInstanceClass());
    };
    String _join = IterableExtensions.join(ListExtensions.map(values, _function), ",");
    _builder.append(_join);
    _builder.append("]");
    return _builder;
  }
  
  protected CharSequence _transformAttributeValue(final String value, final Class<?> type) {
    String cleanValue = value.replaceAll("[\\W]|_", " ");
    int _length = cleanValue.length();
    boolean _greaterThan = (_length > 10);
    if (_greaterThan) {
      String _substring = cleanValue.substring(0, 10);
      String _plus = (_substring + "...");
      cleanValue = _plus;
    }
    return cleanValue;
  }
  
  protected CharSequence _transformAttributeValue(final Double value, final Class<?> type) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(value);
    return _builder;
  }
  
  protected CharSequence _transformAttributeValue(final Integer value, final Class<?> type) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(value);
    return _builder;
  }
  
  protected CharSequence _transformAttributeValue(final Boolean value, final Class<?> type) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(value);
    return _builder;
  }
  
  protected CharSequence _transformAttributeValue(final Object value, final Class<?> type) {
    CharSequence _xifexpression = null;
    boolean _isEnum = type.isEnum();
    if (_isEnum) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("::");
      String _string = value.toString();
      _builder.append(_string);
      _xifexpression = _builder;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      String _simpleName = type.getSimpleName();
      _builder_1.append(_simpleName);
      return _builder_1.toString();
    }
    return _xifexpression;
  }
  
  protected CharSequence transformRelation(final EReference reference, final EObject source, final EObject target, final Map<EObject, Integer> objectToID) {
    CharSequence _xifexpression = null;
    if ((((!Objects.equal(source, null)) && (!Objects.equal(target, null))) && objectToID.containsKey(target))) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("edge");
      _builder.newLine();
      _builder.append("[");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("source\t");
      Integer _get = objectToID.get(source);
      _builder.append(_get, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("target\t");
      Integer _get_1 = objectToID.get(target);
      _builder.append(_get_1, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("graphics");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("[");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("fill\t\"#000000\"");
      _builder.newLine();
      {
        boolean _isContainment = reference.isContainment();
        if (_isContainment) {
          _builder.append("\t\t");
          _builder.append("width\t3");
          _builder.newLine();
        }
      }
      _builder.append("\t\t");
      _builder.append("targetArrow\t\"standard\"");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("]");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("LabelGraphics");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("[");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("text\t\"");
      String _name = reference.getName();
      _builder.append(_name, "\t\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("fontSize\t14");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("fontName\t\"Consolas\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("configuration\t\"AutoFlippingLabel\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("model\t\"six_pos\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("position\t\"thead\"");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("]");
      _builder.newLine();
      _builder.append("]");
      _builder.newLine();
      _xifexpression = _builder;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      return _builder_1.toString();
    }
    return _xifexpression;
  }
  
  protected CharSequence transformAttribute(final EAttribute attribute, final Object values) {
    if (values instanceof List) {
      return _transformAttribute(attribute, (List<?>)values);
    } else if (values == null) {
      return _transformAttribute(attribute, (Void)null);
    } else if (values != null) {
      return _transformAttribute(attribute, values);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(attribute, values).toString());
    }
  }
  
  protected CharSequence transformAttributeValue(final Object value, final Class<?> type) {
    if (value instanceof Double) {
      return _transformAttributeValue((Double)value, type);
    } else if (value instanceof Integer) {
      return _transformAttributeValue((Integer)value, type);
    } else if (value instanceof Boolean) {
      return _transformAttributeValue((Boolean)value, type);
    } else if (value instanceof String) {
      return _transformAttributeValue((String)value, type);
    } else if (value != null) {
      return _transformAttributeValue(value, type);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(value, type).toString());
    }
  }
}
