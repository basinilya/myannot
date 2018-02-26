package org.foo.annotations.processors;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.foo.annotations.GenerateEquals;

@SupportedAnnotationTypes("org.foo.annotations.GenerateEquals")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class GenerateEqualsProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		for (Element e : roundEnv
				.getElementsAnnotatedWith(GenerateEquals.class)) {
			//GenerateEquals complexity = e.getAnnotation(GenerateEquals.class);
			String message = "annotation found in " + e.getSimpleName()
			// + " with complexity " + complexity.value()
			;
			processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
					message);

			if (e.getKind() == ElementKind.CLASS) {
				TypeElement classElement = (TypeElement) e;
				PackageElement packageElement = (PackageElement) classElement
						.getEnclosingElement();

				JavaFileObject jfo;
				try {
					Name m = classElement.getSimpleName();
					String n = m + "Equals";
					String s = classElement.getQualifiedName() + "Equals";
					jfo = processingEnv.getFiler().createSourceFile(s);

					BufferedWriter bw = new BufferedWriter(jfo.openWriter());
					Name s2 = packageElement.getQualifiedName();
					if (s2.length() != 0) {
						bw.append("package ");
						bw.append(s2);
						bw.append(";");
						bw.newLine();
						bw.newLine();
					}
					bw.append("public class ").append(n).append(" {");
					bw.newLine();
					bw.append("public static boolean equals(").append(m).append(" _this, Object other) { return false; }");
					bw.newLine();
					bw.append("}");
					bw.newLine();
					bw.close();
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}

			}
		}
		return true; // no further processing of this annotation type
	}
}
