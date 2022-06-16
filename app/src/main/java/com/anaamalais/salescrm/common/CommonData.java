package com.anaamalais.salescrm.common;

import com.anaamalais.salescrm.List.ExterioraccessoriesList;
import com.anaamalais.salescrm.List.InterioraccessoriesList;
import com.anaamalais.salescrm.List.UtilityaccessoriesList;

import java.util.ArrayList;
import java.util.List;

public class CommonData {

    public interface getInteriorData{
       static List<InterioraccessoriesList> interioraccessoriesLists = new ArrayList<>();
       static List<ExterioraccessoriesList> exterioraccessoriesLists = new ArrayList<>();
       static List<UtilityaccessoriesList> utilityaccessoriesLists  = new ArrayList<>();

    }

    public interface getExteriorData{
//        static List<ExterioraccessoriesList> exterioraccessoriesLists = new ArrayList<>();

    }
}
