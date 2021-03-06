package com.izforge.izpack.installer.container;

import com.izforge.izpack.api.container.BindeableContainer;
import com.izforge.izpack.api.data.ResourceManager;
import com.izforge.izpack.api.substitutor.VariableSubstitutor;
import com.izforge.izpack.core.container.AbstractContainer;
import com.izforge.izpack.core.substitutor.VariableSubstitutorImpl;
import com.izforge.izpack.installer.automation.AutomatedInstaller;
import com.izforge.izpack.installer.container.provider.IconsProvider;
import com.izforge.izpack.installer.data.UninstallData;
import com.izforge.izpack.installer.data.UninstallDataWriter;
import com.izforge.izpack.installer.language.ConditionCheck;
import com.izforge.izpack.installer.language.LanguageDialog;
import com.izforge.izpack.installer.manager.PanelManager;
import com.izforge.izpack.merge.resolve.PathResolver;

import com.izforge.izpack.test.provider.GUIInstallDataMockProvider;
import org.fest.swing.fixture.DialogFixture;
import org.mockito.Mockito;
import org.picocontainer.Characteristics;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.injectors.ProviderAdapter;
import org.picocontainer.parameters.ComponentParameter;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Container for test language
 *
 * @author Anthonin Bonnefoy
 */
public class TestLanguageContainer extends AbstractContainer
{

    /**
     * Init component bindings
     */
    public void fillContainer(MutablePicoContainer pico)
    {
        pico.addComponent(System.getProperties());

        ResourceManager resourceManager = Mockito.mock(ResourceManager.class);
        pico.addComponent(VariableSubstitutor.class, VariableSubstitutorImpl.class)
                .addComponent(resourceManager)
                .addComponent(Mockito.mock(ConditionCheck.class))
                .addComponent(Mockito.mock(UninstallData.class))
                .addComponent(Mockito.mock(UninstallDataWriter.class))
                .addComponent(Mockito.mock(AutomatedInstaller.class))
                .addComponent(Mockito.mock(PathResolver.class))
                .addComponent(Mockito.mock(PanelManager.class))
                .addComponent(DialogFixture.class, DialogFixture.class, new ComponentParameter(LanguageDialog.class))
                .addComponent(BindeableContainer.class, this)
                .as(Characteristics.USE_NAMES).addComponent(LanguageDialog.class)
                .addConfig("frame", initFrame())
                .addConfig("title", "testPanel");
        pico
                .addAdapter(new ProviderAdapter(new GUIInstallDataMockProvider()))
                .addAdapter(new ProviderAdapter(new IconsProvider()));

        Mockito.when(resourceManager.getAvailableLangPacks()).thenReturn(Arrays.asList("eng", "fra"));
        ImageIcon engFlag = new ImageIcon(getClass().getResource("/com/izforge/izpack/bin/langpacks/flags/eng.gif"));
        ImageIcon frFlag = new ImageIcon(getClass().getResource("/com/izforge/izpack/bin/langpacks/flags/fra.gif"));
        Mockito.when(resourceManager.getImageIconResource("flag.eng")).thenReturn(engFlag);
        Mockito.when(resourceManager.getImageIconResource("flag.fra")).thenReturn(frFlag);
        Mockito.when(resourceManager.getInputStream(Mockito.anyString())).thenReturn(getClass().getResourceAsStream("/com/izforge/izpack/bin/langpacks/installer/eng.xml"));
    }

    private JFrame initFrame()
    {
        // Dummy Frame
        JFrame frame = new JFrame();
        Dimension frameSize = frame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2 - 10);
        return frame;
    }
}
